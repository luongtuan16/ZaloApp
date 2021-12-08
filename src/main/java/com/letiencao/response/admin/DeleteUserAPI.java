package com.letiencao.response.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.letiencao.api.BaseHTTP;
import com.letiencao.model.AccountModel;
import com.letiencao.model.ConversationModel;
import com.letiencao.model.PostModel;
import com.letiencao.response.BaseResponse;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IBlockUserService;
import com.letiencao.service.IBlocksService;
import com.letiencao.service.ICommentService;
import com.letiencao.service.IConversationService;
import com.letiencao.service.IFriendService;
import com.letiencao.service.ILikesService;
import com.letiencao.service.IMessageService;
import com.letiencao.service.IPostService;
import com.letiencao.service.IReportService;
import com.letiencao.service.ISearchService;
import com.letiencao.service.IVerifyCodeService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.BlockUserService;
import com.letiencao.service.impl.BlocksService;
import com.letiencao.service.impl.CommentService;
import com.letiencao.service.impl.ConversationService;
import com.letiencao.service.impl.FriendService;
import com.letiencao.service.impl.LikesService;
import com.letiencao.service.impl.MessageService;
import com.letiencao.service.impl.PostService;
import com.letiencao.service.impl.ReportService;
import com.letiencao.service.impl.SearchService;
import com.letiencao.service.impl.VerifyCodeService;

@WebServlet("/api/delete_user")
public class DeleteUserAPI extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private IAccountService accountService;
	private GenericService genericService;
	private IFriendService friendService;
	private IPostService postService;
	private IBlocksService blocksService;
	private IBlockUserService blockUserService;
	private IMessageService messageService;
	private IConversationService conversationService;
	private ISearchService searchService;
	private IVerifyCodeService verifyCodeService;
	private ICommentService commentService;
	private IReportService reportService;
	private ILikesService likesService;

	public DeleteUserAPI() {
		friendService = new FriendService();
		accountService = new AccountService();
		genericService = new BaseService();
		postService = new PostService();
		blocksService = new BlocksService();
		blockUserService = new BlockUserService();
		messageService = new MessageService();
		conversationService = new ConversationService();
		searchService = new SearchService();
		verifyCodeService = new VerifyCodeService();
		commentService = new CommentService();
		reportService = new ReportService();
		likesService = new LikesService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		BaseResponse baseResponse = new BaseResponse();

		String jwt = req.getParameter("token");
		String userIdQuery = req.getParameter("user_id");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));

		if (userIdQuery != null) {
			Long userId = Long.valueOf(userIdQuery);
			AccountModel user = accountService.findById(userId);
			if (user != null) {
				if (user.getRoleId() != null && user.getRoleId() >= accountModel.getRoleId()) {
					baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1009));
					baseResponse.setMessage(BaseHTTP.MESSAGE_1009);
					resp.getWriter().print(gson.toJson(baseResponse));
				} else {
					// delete friend
					if (friendService.deleteUserFriends(userId) == false) {
						System.out.println("Fail on deleting friends");
						deleteFail(baseResponse);
						resp.getWriter().print(gson.toJson(baseResponse));
						return;
					}

					// delete post:file, like, report, comment, post
					List<PostModel> posts = postService.findPostByAccountId(userId);
					if (posts.size() > 0)
						for (PostModel postModel : posts) {
							if (postService.deleteById(postModel.getId()) == false) {
								System.out.println("Fail on deleting Posts");
								deleteFail(baseResponse);
								resp.getWriter().print(gson.toJson(baseResponse));
								return;
							}
						}
					// delete comment (in others post)
					if (commentService.deleteByAccountId(userId) == false) {
						System.out.println("Fail on deleting Comments");
						deleteFail(baseResponse);
						resp.getWriter().print(gson.toJson(baseResponse));
						return;
					}
					// delete report (the others)
					if (reportService.deleteByAccountId(userId) == false) {
						System.out.println("Fail on deleting Reports");
						deleteFail(baseResponse);
						resp.getWriter().print(gson.toJson(baseResponse));
						return;
					}
					// delete likes (in others post)
					if (likesService.deleteByAccountId(userId) == false) {
						System.out.println("Fail on deleting Likes");
						deleteFail(baseResponse);
						resp.getWriter().print(gson.toJson(baseResponse));
						return;
					}
					// delete block diary
					if (blocksService.deleteUserBlocks(userId) == false) {
						System.out.println("Fail on deleting Blocks");
						deleteFail(baseResponse);
						resp.getWriter().print(gson.toJson(baseResponse));
						return;
					}
					// delete block message
					if (blockUserService.deleteUserBlocks(userId) == false) {
						System.out.println("Fail on deleting Blocks Message");
						deleteFail(baseResponse);
						resp.getWriter().print(gson.toJson(baseResponse));
						return;
					}
					// delete message --> delete conversation
					List<ConversationModel> conversations = conversationService.findListConversation(userId);
					for (ConversationModel conversation : conversations) {
						if (messageService.deleteConversation(conversation.getId()) == false) {
							System.out.println("Fail on deleting Messages");
							deleteFail(baseResponse);
							resp.getWriter().print(gson.toJson(baseResponse));
							return;
						}
						if (conversationService.deleteConversation(conversation.getId()) == false) {
							System.out.println("Fail on deleting Conversation");
							deleteFail(baseResponse);
							resp.getWriter().print(gson.toJson(baseResponse));
							return;
						}
					}
					// delete search
					if (searchService.deleteByAccountId(userId) == false) {
						System.out.println("Fail on deleting searchs");
						deleteFail(baseResponse);
						resp.getWriter().print(gson.toJson(baseResponse));
						return;
					}
					// delete verify code
					if (verifyCodeService.deleteByPhoneNumber(user.getPhoneNumber()) == false) {
						System.out.println("Fail on deleting Verify Codes");
						deleteFail(baseResponse);
						resp.getWriter().print(gson.toJson(baseResponse));
						return;
					}
					// delete account
					if (accountService.deleteAccount(userId) == false) {
						System.out.println("Fail on deleting Account");
						deleteFail(baseResponse);
						resp.getWriter().print(gson.toJson(baseResponse));
						return;
					}
					baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
					baseResponse.setMessage(BaseHTTP.MESSAGE_1000);
				}
			} else {
				baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
				baseResponse.setMessage(BaseHTTP.MESSAGE_1004);
			}
		} else {
			baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1002));
			baseResponse.setMessage(BaseHTTP.MESSAGE_1002);
		}
		resp.getWriter().print(gson.toJson(baseResponse));
	}

	private void deleteFail(BaseResponse baseResponse) {
		baseResponse.setCode(String.valueOf(BaseHTTP.CODE_1005));
		baseResponse.setMessage(BaseHTTP.MESSAGE_1005);
	}
}
