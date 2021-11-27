package com.letiencao.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.letiencao.dao.IAccountDAO;
import com.letiencao.dao.ICommentDAO;
import com.letiencao.dao.IFileDAO;
import com.letiencao.dao.ILikesDAO;
import com.letiencao.dao.IPostDAO;
import com.letiencao.dao.IReportDAO;
import com.letiencao.dao.impl.AccountDAO;
import com.letiencao.dao.impl.CommentDAO;
import com.letiencao.dao.impl.FileDAO;
import com.letiencao.dao.impl.LikesDAO;
import com.letiencao.dao.impl.PostDAO;
import com.letiencao.dao.impl.ReportDAO;
import com.letiencao.model.AccountModel;
import com.letiencao.model.PostModel;
import com.letiencao.request.post.AddPostRequest;
import com.letiencao.service.IPostService;

public class PostService extends BaseService implements IPostService {

	private IPostDAO postDAO;
	private IAccountDAO accountDAO;
	
	public PostService() {
		postDAO = new PostDAO();
		accountDAO = new AccountDAO();
	}

	@SuppressWarnings("unused")
	@Override
	public Long insertOne(AddPostRequest addPostRequest) {
		String described = addPostRequest.getDescribed();
		String phoneNumber = getPhoneNumberFromToken(addPostRequest.getToken());
		AccountModel model = accountDAO.findByPhoneNumber(phoneNumber);
		PostModel postModel = new PostModel();
		Long accountId = model.getId();
		postModel.setAccountId(accountId);
		postModel.setContent(described);
		postModel.setCreatedBy(model.getPhoneNumber());
		postModel.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		postModel.setDeleted(false);
		Long id = postDAO.insertOne(postModel);
		return id;
	}

	@Override
	public PostModel findPostById(Long id) {
		PostModel postModel = postDAO.findPostById(id);
		// System.out.println(postDAO.findPostById(id));
		if (postModel == null || postModel.isDeleted())
			return null;
		else
			return postModel;
		// return postDAO.findPostById(id);
	}

	@Override
	public PostModel findById(Long id) {
		return postDAO.findById(id);
	}

//	@Override
//	public boolean deleteById(Long id) {
//		PostModel model = findById(id);
//		try {
//			if (model.isDeleted() == false) {
//				return postDAO.deleteById(id);
//
//			} else {
//				return false;
//			}
//		} catch (NullPointerException e) {
//			return false;
//		}
//	}
	@Override
	public boolean deleteById(Long id) {
		PostModel model = findById(id);
		if (model != null)
			try {
				IFileDAO fileDAO = new FileDAO();
				ILikesDAO likesDAO= new LikesDAO();
				IReportDAO reportDAO = new ReportDAO();
				ICommentDAO commentDAO = new CommentDAO();
				commentDAO.deleteByPostId(id);
				fileDAO.deleteByPostId(id);
				likesDAO.deleteByPostId(id);
				reportDAO.deleteByPostId(id);
				return postDAO.deleteById(id);
			} catch (NullPointerException e) {
				return false;
			}
		return false;
	}

	@Override
	public List<PostModel> findAll() {
		return postDAO.findAll();
	}

	@Override
	public Long findAccountIdByPostId(Long id) {
		return postDAO.findAccountIdByPostId(id);
	}

	@Override
	public List<PostModel> findPostByAccountId(Long accountId) {
		List<PostModel> list = postDAO.findPostByAccountId(accountId);
		List<PostModel> list2 = new ArrayList<PostModel>();
		for (int i = 0; i < list.size() - 1; i++) {
			PostModel model = list.get(i);
			if (model.getId() == list.get(i + 1).getId()) {
				continue;
			} else {
				list2.add(model);
			}

		}
		list2.add(list.get(list.size() - 1));
		return list2;
	}

	@Override
	public boolean updateContenById(Long id, String content) {
		return postDAO.updateContentById(id, content);
	}

}
