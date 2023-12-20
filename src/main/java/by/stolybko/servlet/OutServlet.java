package by.stolybko.servlet;

import by.stolybko.dto.UserResponseDTO;
import by.stolybko.printer.Printer;
import by.stolybko.printer.PrinterType;
import by.stolybko.service.UserService;
import by.stolybko.service.impl.UserServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/generate/")
public class OutServlet extends HttpServlet {

    private final Printer printer = Printer.getInstance();
    private final UserService userService = new UserServiceImpl();
    private final PrinterType printerType = PrinterType.PDF;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String id = req.getParameter("id");
            if (id == null) {
                int page = 1;
                int pageSize = 20;
                if(req.getParameter("page") != null) {
                    page = Integer.parseInt(req.getParameter("page"));
                }
                if(req.getParameter("pageSize") != null) {
                    pageSize = Integer.parseInt(req.getParameter("pageSize"));
                }
                List<UserResponseDTO> users = userService.getAll(pageSize, (page - 1) * pageSize);

                printer.print(users, printerType);

                resp.setContentType("text/html;charset=UTF-8");
                resp.setStatus(200);
                resp.getWriter().write("Документ создан");


            } else {

                UserResponseDTO user = userService.getUserById(Long.valueOf(id));

                printer.print(user, printerType);

                resp.setContentType("text/html;charset=UTF-8");
                resp.setStatus(200);
                resp.getWriter().write("Документ создан");
            }

        } catch (Exception e) {
            resp.setStatus(400);
        }
    }
}
