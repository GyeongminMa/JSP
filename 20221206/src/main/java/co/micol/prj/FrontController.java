package co.micol.prj;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import co.micol.prj.common.Command;
import co.micol.prj.member.command.MemberList;


@WebServlet("*.do")
public class FrontController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private HashMap<String, Command> map = new HashMap<String, Command>();
	
    public FrontController() {
        super();

    }

	
	public void init(ServletConfig config) throws ServletException {
		//명령(Command)를 저장하는 영역
		map.put("/main.do", new MainCommand());//처음페이지
		map.put("/memberList.do", new MemberList());//멤버목록보기
	}

	
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Controller 본체
		request.setCharacterEncoding("utf-8");//한글깨짐 방지
		String uri = request.getRequestURI();//uri 값을 읽어온다.
		String contextPath = request.getContextPath();//ContextPath 를 읽어온다.
		String page = uri.substring(contextPath.length());//실제요청명을 구한다.
	
		Command command = map.get(page);//수행할 command를 찾고
		String viewPage = command.exec(request, response);//찾은 command를 실행
		
		if(!viewPage.endsWith(".do")) {
			viewPage = "WEB-INF/views/" + viewPage+ ".jsp";
			
			RequestDispatcher dispatcher = request.getRequestDispatcher(viewPage);
			dispatcher.forward(request, response);
		}else {
			response.sendRedirect(viewPage);
		}
		
	
	}

}
