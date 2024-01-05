
package by.stolybko.servlet;

import by.stolybko.api.DeSerializer;
import by.stolybko.api.Serializer;
import by.stolybko.dto.UserRequestDTO;
import by.stolybko.dto.UserResponseDTO;
import by.stolybko.exception.ValidationException;
import by.stolybko.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserServlet extends HttpServlet {

    private final UserService userService;
    private final DeSerializer deSerializer;
    private final Serializer serializer;


    /**
     * метод предоставляет операцию READ
     * Если запрос содержит параметр "id" - выводится сущность с указанным id
     * Если запрос не содержит параметр "id" - выводятся все сущности
     * В случае успеха операции возвращается ответ с status code "200"
     * В случае ошибки операции возвращается ответ с status code "400"
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String id = req.getParameter("id");
            if (id == null) {
                int page = 1;
                int pageSize = 20;
                if (req.getParameter("page") != null) {
                    page = Integer.parseInt(req.getParameter("page"));
                }
                if (req.getParameter("pageSize") != null) {
                    pageSize = Integer.parseInt(req.getParameter("pageSize"));
                }
                List<UserResponseDTO> users = userService.getAll(pageSize, (page - 1) * pageSize);

                resp.setContentType("application/json");
                resp.setStatus(200);
                StringBuilder sb = new StringBuilder("[");

                for (UserResponseDTO userResponseDTO : users) {
                    sb.append(serializer.serializingInJson(userResponseDTO));
                    sb.append(", ");
                }
                sb.deleteCharAt(sb.lastIndexOf(","));
                sb.append("]");
                resp.getWriter().write(sb.toString());


            } else {

                UserResponseDTO user = userService.getUserById(Long.valueOf(id));

                resp.setContentType("application/json");
                resp.setStatus(200);

                String jsonResponse = serializer.serializingInJson(user);
                resp.getWriter().write(jsonResponse);
            }

        } catch (Exception e) {
            resp.setStatus(400);
        }
    }


    /**
     * метод предоставляет операцию CREATE
     * В случае успеха операции возвращается ответ с status code "200"
     * В случае ошибки операции возвращается ответ с status code "400" или "404"
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        try {
            UserRequestDTO requestDTO = deSerializer.deSerializingJson(UserRequestDTO.class, req.getReader().lines().collect(Collectors.joining()));
            UserResponseDTO userResponseDTO = userService.save(requestDTO);

            resp.setContentType("application/json; charset=UTF-8");
            resp.setStatus(200);

            String jsonResponse = serializer.serializingInJson(userResponseDTO);
            resp.getWriter().write(jsonResponse);
        } catch (ValidationException e) {
            resp.setStatus(400);
            resp.getWriter().write(e.getErrors().toString());
        } catch (Exception e) {
            resp.setStatus(404);
        }
    }


    /**
     * метод предоставляет операцию UPDATE
     * обновляется сущность с указанным id
     * В случае успеха операции возвращается ответ с status code "200"
     * В случае ошибки операции возвращается ответ с status code "400" или "404"
     */

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String id = req.getParameter("id");

        try {
            if (id != null) {
                UserRequestDTO requestDTO = deSerializer.deSerializingJson(UserRequestDTO.class, req.getReader().lines().collect(Collectors.joining()));
                UserResponseDTO userResponseDTO = userService.update(requestDTO, Long.valueOf(id));

                resp.setContentType("application/json; charset=UTF-8");
                resp.setStatus(200);

                String jsonResponse = serializer.serializingInJson(userResponseDTO);
                resp.getWriter().write(jsonResponse);

            } else {
                resp.setStatus(400);
            }
        } catch (ValidationException e) {
            resp.setStatus(400);
            resp.getWriter().write(e.getErrors().toString());
        } catch (Exception e) {
            resp.setStatus(404);
        }
    }


    /**
     * метод предоставляет операции DELETE
     * удаляется сущность с id равным значению параметра delete
     * В случае успеха операции возвращается ответ с status code "200"
     * В случае ошибки операции возвращается ответ с status code "400"
     */

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String delete = req.getParameter("delete");
        if (delete != null) {
            userService.delete(Long.valueOf(delete));
            resp.setStatus(200);
        } else {
            resp.setStatus(400);
        }
    }
}

