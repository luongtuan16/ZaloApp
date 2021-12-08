package com.letiencao.api.admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.letiencao.api.BaseHTTP;
import com.letiencao.model.AccountModel;
import com.letiencao.model.FriendModel;
import com.letiencao.response.admin.DataGetBUIFResp;
import com.letiencao.response.admin.GetBasicUserInfoResp;
import com.letiencao.service.GenericService;
import com.letiencao.service.IAccountService;
import com.letiencao.service.IFriendService;
import com.letiencao.service.impl.AccountService;
import com.letiencao.service.impl.BaseService;
import com.letiencao.service.impl.FriendService;

@WebServlet("/api/get_basic_user_info")
public class GetBasicUserInfoAPI extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private IAccountService accountService;
	private GenericService genericService;
	private IFriendService friendService;

	public GetBasicUserInfoAPI() {
		// TODO Auto-generated constructor stub
		accountService = new AccountService();
		genericService = new BaseService();
		friendService = new FriendService();
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/json");
		Gson gson = new Gson();
//		GetUserInfoRequest getUserInfoRequest = gson.fromJson(request.getReader(), GetUserInfoRequest.class);
		String jwt = request.getParameter("token");
		String userIdQuery = request.getParameter("user_id");
		//String roleKeyQuery = request.getParameter("role_key");

		GetBasicUserInfoResp userInfoResp = new GetBasicUserInfoResp();
		DataGetBUIFResp data = new DataGetBUIFResp();
		List<FriendModel> list = new ArrayList<FriendModel>();

		AccountModel accountModel = accountService.findByPhoneNumber(genericService.getPhoneNumberFromToken(jwt));
		try {
			if (userIdQuery != null) {
				Long userId = Long.valueOf(userIdQuery);
				// Long roleKey = Long.valueOf(roleKeyQuery);
				if (userId.toString() != null) {
					// Check userId existed
					AccountModel a = accountService.findById(userId);
					if (a != null && a.isActive()) {
						if ((accountModel.getRoleId() == null)
								|| (a.getRoleId() != null && a.getRoleId() >= accountModel.getRoleId())) {
							// Not permission
							userInfoResp.setData(null);
							userInfoResp.setCode(String.valueOf(BaseHTTP.CODE_1009));
							userInfoResp.setMessage(BaseHTTP.MESSAGE_1009);
							response.getWriter().print(gson.toJson(userInfoResp));
							return;
						}
						// Get info
						list = friendService.findListFriendById(userId);
						List<String> images = new ArrayList<String>();
						if (a.getAvatar() != null)
							images.add(a.getAvatar());
						if (a.getCover_image() != null)
							images.add(a.getCover_image());

						setResponse(userInfoResp, data, a, list, images);

					} else {
						// User invalidate
						userInfoResp.setData(null);
						userInfoResp.setCode(String.valueOf(BaseHTTP.CODE_9995));
						userInfoResp.setMessage(BaseHTTP.MESSAGE_9995);
					}

				} else {
					// Parameter not enough
					userInfoResp.setData(null);
					userInfoResp.setCode(String.valueOf(BaseHTTP.CODE_1002));
					userInfoResp.setMessage(BaseHTTP.MESSAGE_1002);
				}
			} else {
				// Parameter not enough
				userInfoResp.setData(null);
				userInfoResp.setCode(String.valueOf(BaseHTTP.CODE_1002));
				userInfoResp.setMessage(BaseHTTP.MESSAGE_1002);
			}
		} catch (NumberFormatException | JsonSyntaxException e) {
			userInfoResp.setData(null);
			userInfoResp.setCode(String.valueOf(BaseHTTP.CODE_1003));
			userInfoResp.setMessage(BaseHTTP.MESSAGE_1003);
		}

		response.getWriter().print(gson.toJson(userInfoResp));
	}

	// get data response
	public void setResponse(GetBasicUserInfoResp userInfoResp, DataGetBUIFResp data, AccountModel accountModel,
			List<FriendModel> list, List<String> images) {

		data.setUser_id(accountModel.getId());
		data.setUser_name(accountModel.getName());
		data.setAddress(accountModel.getAddress());
		data.setPhonenumber(accountModel.getPhoneNumber());
		data.setEmail("");
		data.setImage(images);
		data.setFriend_count(list.size());
		data.setOnline(false);

		userInfoResp.setCode(String.valueOf(BaseHTTP.CODE_1000));
		userInfoResp.setMessage(BaseHTTP.MESSAGE_1000);
		userInfoResp.setData(data);
	}

}
