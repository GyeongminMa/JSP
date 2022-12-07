package co.micol.prj.web;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.micol.prj.MainCommand;
import co.micol.prj.common.Command;
import co.micol.prj.member.command.AjaxMemberIdCheck;
import co.micol.prj.member.command.MemberJoin;
import co.micol.prj.member.command.MemberJoinForm;
import co.micol.prj.member.command.MemberList;


//@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private HashMap<String,Command> map = new HashMap<String,Command>();   

    public FrontController() {
        super();
      
    }

	
	public void init(ServletConfig config) throws ServletException {
		// 명령집단
		map.put("/main.do", new MainCommand()); //처음실행하는 페이지
		map.put("/memberList.do", new MemberList()); //멤버목록 보기
		map.put("/memberJoinForm.do", new MemberJoinForm());//회원가입폼
		map.put("/AjaxMemberIdCheck.do", new AjaxMemberIdCheck());//회원아이디
		map.put("/memberJoin.do", new MemberJoin());//회원가입처리
	}

	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 요청을 분석 실행 결과를 돌려주는곳
		request.setCharacterEncoding("utf-8");
		String uri = request.getRequestURI();
		String contexPath = request.getContextPath();
		String page = uri.substring(contexPath.length());
		
		Command command = map.get(page);
		String viewPage = command.exec(request, response);
		
		//view Resolve
		if(!viewPage.endsWith(".do")) {
			
			if(viewPage.startsWith("Ajax:")) {
				//ajax 새로만든 index.jsp에서 contenttype보기
				response.setContentType("text/html; charset=UTF-8");
				response.getWriter().print(viewPage.substring(5));
				return;
				
			}else if(!viewPage.endsWith(".tiles")) {
				
				viewPage = "WEB-INF/views/" + viewPage + ".jsp";	//타일즈 적용안하는것
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}else {
			response.sendRedirect(viewPage);
		}
		//view Resolve end
			
	}

}
