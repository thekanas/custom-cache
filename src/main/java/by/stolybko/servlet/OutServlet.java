
package by.stolybko.servlet;

import by.stolybko.dto.UserResponseDTO;
import by.stolybko.printer.Printer;
import by.stolybko.printer.PrinterType;
import by.stolybko.service.UserService;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutServlet extends HttpServlet {

    private final Printer printer;
    private final UserService userService;

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        try {
            String id = req.getParameter("id");
            PrinterType printerType = PrinterType.PDF;
            byte[] output;
            if (id == null) {
                List<UserResponseDTO> dtoList = userService.getAll();
                output = printer.getDocAsBytes(dtoList, printerType);

            } else {
                UserResponseDTO dto = userService.getUserById(Long.valueOf(id));
                output = printer.getDocAsBytes(dto, printerType);
            }

            resp.setHeader("Content-Disposition", "attachment; filename=\"filename.pdf\"");
            resp.setContentType("application/pdf");
            resp.setCharacterEncoding(StandardCharsets.UTF_8.name());

            resp.getOutputStream().write(output);
        } catch (Exception e) {
            resp.setStatus(400);
        }
    }
}

