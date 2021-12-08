package com.letiencao.api.post;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.letiencao.api.BaseHTTP;
import com.letiencao.model.AccountModel;
import com.letiencao.model.BlocksModel;
import com.letiencao.model.FileModel;
import com.letiencao.model.PostModel;
import com.letiencao.request.post.GetListPostRequest;
import com.letiencao.response.post.AuthorGetPostResponse;
import com.letiencao.response.post.DataGetListPost;
import com.letiencao.response.post.GetListPostResponse;
import com.letiencao.response.post.ListPostResponse;
import com.letiencao.response.post.VideoGetPostResponse;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IBlocksService;
import com.letiencao.service.ICommentService;
import com.letiencao.service.IFileService;
import com.letiencao.service.ILikesService;
import com.letiencao.service.IPostService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.BlocksService;
import com.letiencao.service.impl.CommentService;
import com.letiencao.service.impl.FileService;
import com.letiencao.service.impl.LikesService;
import com.letiencao.service.impl.PostService;

@WebServlet("/api/get_list_posts")
public class GetListPostAPI extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GenericService genericService;
	private IPostService postService;
	private IAccountService accountService;
	private ILikesService likesService;
	private ICommentService commentService;
	private IBlocksService blocksService;
	private IFileService fileService;

	public GetListPostAPI() {
		genericService = new BaseService();
		postService = new PostService();
		accountService = new AccountService();
		likesService = new LikesService();
		commentService = new CommentService();
		blocksService = new BlocksService();
		fileService = new FileService();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		GetListPostResponse getListPostResponse = new GetListPostResponse();
		DataGetListPost dataGetListPost = new DataGetListPost();
		
		GetListPostRequest getListPostRequest = gson.fromJson(request.getReader(), GetListPostRequest.class);
		String jwt = request.getParameter("token");
				
		List<ListPostResponse> listPostResponses = new ArrayList<ListPostResponse>();
		if (getListPostRequest != null) {
			if (String.valueOf(getListPostRequest.getCount()) != null) {
				if (String.valueOf(getListPostRequest.getCount()).length() > 0) {
					// tim tat ca cac bai viet 			
					AccountModel accountModel = accountService
							.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
					Long accountId = accountModel.getId();
					List<PostModel> list = new ArrayList<PostModel>();
					list = postService.findAll();					
					if (list.size() > 0) {
						int count = getListPostRequest.getCount();
						int index =(int) getListPostRequest.getIndex();
						Long oldLastId=getListPostRequest.getLast_id();
						Long newLastId=list.get(list.size()-1).getId();
						int newItems = 0;
						int checkNewItems=0;
						for (int i = list.size()-1; i >= 0 && count>0; i--) {
							PostModel postModel = list.get(i);
							Long postId = postModel.getId();
							if (postId == oldLastId) {
								//lấy xong newItems items mới, bỏ qua index-1 items đã lấy từ những lần trước
								dataGetListPost.setNew_items(newItems);
								i=i-index+1;
								checkNewItems=1;
								i--;
								continue;
							}
							newItems++;
							ListPostResponse listPostResponse = new ListPostResponse();	
							listPostResponse.setId(postId);
							listPostResponse.setDescribed(postModel.getContent());
							listPostResponse.setCreated(String.valueOf(postModel.getCreatedDate()));
							listPostResponse.setModified(String.valueOf(postModel.getModifiedDate()));
							// amount of like
							int like = likesService.findByPostId(postId);
							listPostResponse.setLike(like);
							// amount of comment
							int comment = commentService.findByPostId(postId);
							listPostResponse.setComment(comment);
							// check this user liked this post
							boolean checkThisUserLiked = likesService.checkThisUserLiked(accountId, postId);
							listPostResponse.setIs_liked(checkThisUserLiked);
							// is_blocked
							BlocksModel blocksModel = blocksService.findOne(postModel.getAccountId(), accountId);
							if (blocksModel == null) {
								listPostResponse.setIs_blocked("UnBlocked");
							} else {
								listPostResponse.setIs_blocked("Blocked");
							}
							// can_edit
							if (accountId == postModel.getAccountId()) {
								listPostResponse.setCan_edit("Can Edit");
							} else {
								listPostResponse.setCan_edit("Can't Edit");
							}
							if (listPostResponse.getIs_blocked().equalsIgnoreCase("Blocked")) {
								listPostResponse.setCan_comment("Can't Comment");
							} else {
								listPostResponse.setCan_comment("Can Comment");
							}
							//images and video
							List<String> images = new ArrayList<String>();
							VideoGetPostResponse video = new VideoGetPostResponse();
							List<FileModel> listFiles = fileService.findByPostId(postId);
							if (listFiles == null) {
								images = null;
								video = null;
							} else {
								System.out.println("size = " + fileService.findByPostId(postId).size());
								for (FileModel fileModel : listFiles) {
									if (fileModel.getContent().endsWith(".jpg")
											|| fileModel.getContent().endsWith(".svg")
											|| fileModel.getContent().endsWith(".JPEG")
											|| fileModel.getContent().endsWith(".png")) {
								
										images.add(fileModel.getContent());
									} else if (fileModel.getContent().endsWith(".mp4")) {

										video.setThumbnail(fileModel.getContent());
										video.setUrl(fileModel.getContent());
									}
								}
							}
							// get author
							AuthorGetPostResponse authorGetPostResponse = new AuthorGetPostResponse();
							AccountModel author =accountService.findById(postModel.getAccountId());
							authorGetPostResponse.setId(author.getId());
							authorGetPostResponse.setName(author.getName());
							authorGetPostResponse
									.setAvatar(author.getAvatar());
							
							listPostResponse.setAuthor(authorGetPostResponse);
							listPostResponse.setImage(images);
							listPostResponse.setVideo(video);
							// set created modified in long type
							listPostResponse.setCreated(String
									.valueOf(genericService.convertTimestampToSeconds(postModel.getCreatedDate())));
							if (postModel.getModifiedDate() != null) {
								listPostResponse.setModified(String.valueOf(
										genericService.convertTimestampToSeconds(postModel.getModifiedDate())));
							}
							listPostResponses.add(listPostResponse);
							count--;
						}
						if (checkNewItems==0) {
							//chưa set newItems
							newItems = 0;
							for (int j=list.size()-1;j>=0;j--) {
								PostModel postModel = list.get(j);
								Long postId = postModel.getId();
								if (postId != oldLastId) newItems++;
								else break;
							}
							dataGetListPost.setNew_items(newItems);
						}
						dataGetListPost.setLast_id(newLastId);
						dataGetListPost.setPosts(listPostResponses);
						getListPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
						getListPostResponse.setMessage(BaseHTTP.MESSAGE_1000);
						getListPostResponse.setData(dataGetListPost);
					
					} else {
						getListPostResponse.setCode(String.valueOf(BaseHTTP.CODE_9995));
						getListPostResponse.setMessage(BaseHTTP.MESSAGE_9995);
						getListPostResponse.setData(null);
					}

				} else {
					getListPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
					getListPostResponse.setMessage(BaseHTTP.MESSAGE_1004);
					getListPostResponse.setData(null);
				}
			} else {
				getListPostResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
				getListPostResponse.setMessage(BaseHTTP.MESSAGE_1002);
				getListPostResponse.setData(null);
			}

			response.getWriter().print(gson.toJson(getListPostResponse));

		}

	}

}
