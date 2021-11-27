package com.letiencao.api.friend;

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
import com.letiencao.model.FriendModel;
import com.letiencao.request.friend.DataGetRequestedFriend;
import com.letiencao.request.friend.GetRequestedFriendRequest;
import com.letiencao.response.friend.FriendsResponse;
import com.letiencao.response.friend.GetRequestedFriendResponse;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IFriendService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.FriendService;

@WebServlet("/api/get-requested-friend")
public class GetRequestedFriendAPI extends HttpServlet {
	/**
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IFriendService friendService;
	private IAccountService accountService;
	private GenericService genericService;

	public GetRequestedFriendAPI() {
		genericService = new BaseService();
		friendService = new FriendService();
		accountService = new AccountService();

	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
		String jwt = request.getParameter("token");
		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		GetRequestedFriendRequest dataRequest = gson.fromJson(request.getReader(), GetRequestedFriendRequest.class);
		
		List<FriendsResponse> friends = new ArrayList<FriendsResponse>();
		DataGetRequestedFriend dataGetRequestedFriend = new DataGetRequestedFriend();
		GetRequestedFriendResponse getRequestedFriendResponse = new GetRequestedFriendResponse();
		
		if (accountModel.getUuid().equals("1")) {
			// request by user
			dataRequest.setUser_id(accountModel.getId());
		} else if (accountModel.getUuid().equals("2")) {
			// request by admin
			if (dataRequest.getUser_id() == null)
				dataRequest.setUser_id(accountModel.getId());
			else {
				//check user exist 
				AccountModel user = accountService.findById(dataRequest.getUser_id());
				if (user == null) {
					getRequestedFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_1004));
					getRequestedFriendResponse.setMessage(BaseHTTP.MESSAGE_1004);
					response.getWriter().print(gson.toJson(getRequestedFriendResponse));
					return ;
				}
			}
		}
		
		
		List<FriendModel> listFriends = friendService.findListFriendRequestByIdB(dataRequest.getUser_id());
		int count = dataRequest.getCount();
		for (int i = dataRequest.getIndex(); i < listFriends.size() && count > 0; i++) {
			AccountModel accountA = accountService.findById(listFriends.get(i).getIdA());
			FriendsResponse friendsResponse = new FriendsResponse();
			friendsResponse.setId(accountA.getId());
			friendsResponse.setAvatar(accountA.getAvatar());
			friendsResponse.setUsername(accountA.getName());
			friendsResponse.setCreated(listFriends.get(i).getCreatedDate());
			friends.add(friendsResponse);
			count--;
		}
		dataGetRequestedFriend.setFriends(friends);
		getRequestedFriendResponse.setData(dataGetRequestedFriend);
		getRequestedFriendResponse.setCode(String.valueOf(BaseHTTP.CODE_1000));
		getRequestedFriendResponse.setMessage(BaseHTTP.MESSAGE_1000);
		response.getWriter().print(gson.toJson(getRequestedFriendResponse));
	}
}
