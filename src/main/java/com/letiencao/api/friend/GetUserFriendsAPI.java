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
import com.letiencao.response.friend.DataGetUserFriendsResp;
import com.letiencao.response.friend.FrsDataGUFrsResp;
import com.letiencao.response.friend.GetUserFriendsResp;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IFriendService;
import com.letiencao.service.IRoleService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.FriendService;
import com.letiencao.service.impl.RoleService;

@WebServlet("/api/get_user_friends")
public class GetUserFriendsAPI extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private IFriendService friendService;
	private IAccountService accountService;
	private GenericService genericService;
	private IRoleService roleService;

	public GetUserFriendsAPI() {
		genericService = new BaseService();
		friendService = new FriendService();
		accountService = new AccountService();
		roleService = new RoleService();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		Gson gson = new Gson();
		GetUserFriendsResp response = new GetUserFriendsResp();
		DataGetUserFriendsResp data = new DataGetUserFriendsResp();
		List<FrsDataGUFrsResp> friends = new ArrayList<FrsDataGUFrsResp>();

		String jwt = req.getParameter("token");
		String indexStr = req.getParameter("index");
		String countStr = req.getParameter("count");
		String userIdStr = req.getParameter("user_id");

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));

		if (indexStr != null && countStr != null) {
			if (accountModel.isActive() == false) {
				response.setCode(String.valueOf(BaseHTTP.CODE_9995));
				response.setMessage(BaseHTTP.MESSAGE_9995);
				resp.getWriter().print(gson.toJson(response));
				return;
			}
			int index = Integer.valueOf(indexStr);
			int count = Integer.valueOf(countStr);
			Long userId = accountModel.getId();
			if (userIdStr != null && accountModel.getRoleId() != null
					&& accountModel.getRoleId() > roleService.findId("user")) {
				userId = Long.valueOf(userIdStr);
			}
			AccountModel user = accountService.findById(userId);
			if (user == null)
				user = accountModel;
			List<Long> listFrId = new ArrayList<Long>();
			List<FriendModel> listFriendModels = friendService.findListFriendById(user.getId());
			for (FriendModel friendModel : listFriendModels) {
				Long idLong = friendModel.getIdA();
				if (idLong == user.getId())
					idLong = friendModel.getIdB();
				if (listFrId.contains(idLong) == false)
					listFrId.add(idLong);
			}

			data.setTotal(listFrId.size());

			for (int i = index; i < listFrId.size() && i < count + index; i++) {
				AccountModel frAccount = accountService.findById(listFrId.get(i));
				FrsDataGUFrsResp frModel = new FrsDataGUFrsResp();

				frModel.setUser_id(frAccount.getId());
				frModel.setUser_name(frAccount.getName());
				frModel.setAvatar(frAccount.getAvatar());
				frModel.setStatus(frAccount.isActive());// trường status này chắc là isOnline, chưa xử lý được

				friends.add(frModel);
			}

			data.setFriends(friends);
			response.setData(data);
			response.setCode(String.valueOf(BaseHTTP.CODE_1000));
			response.setMessage(BaseHTTP.MESSAGE_1000);

		} else {
			response.setCode(String.valueOf(BaseHTTP.CODE_1002));
			response.setMessage(BaseHTTP.MESSAGE_1002);
		}

		resp.getWriter().print(gson.toJson(response));
	}
}
