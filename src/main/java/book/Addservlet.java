package book;

import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.nio.charset.StandardCharsets;

@WebServlet(urlPatterns = "/Add")
@MultipartConfig
public class Addservlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String jdbcUrl = "jdbc:mysql://10.69.219.123:3306/db2";
        String username = "1";
        String password = "13550581080";

        // 从请求中获取需要添加的书籍信息
        String book = req.getParameter("book");
        String author = req.getParameter("author");
        String nation = req.getParameter("nation");
        String content = req.getParameter("content"); // 获取内容
        String price = req.getParameter("price");     // 获取价格


        // 处理图像文件
        Part imagePart = req.getPart("image");
        InputStream imageStream = imagePart.getInputStream();
        byte[] imageBytes = imageStream.readAllBytes();

        // 存储图片到服务器文件系统
        String imagePath = saveImageToServer(imageBytes);

        // 设置响应的 Content-Type
        resp.setContentType("application/json;charset=UTF-8");

        // 调用 Dataitem 类中的添加方法，包括内容、价格和图像数据
        Dataitem.addDataToDatabase(jdbcUrl, username, password, book, content, price, author, nation, imagePath);


        // 返回成功响应
        resp.getWriter().write("{\"success\": true}");
    }

    // 存储图片到服务器文件系统，并返回文件路径
    private String saveImageToServer(byte[] imageBytes) throws IOException {
        // 获取服务器上的存储目录，你可以根据需要修改
        String uploadPath = getServletContext().getRealPath("uploads");

        // 确保目录存在
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        // 生成唯一的文件名
        String fileName = System.currentTimeMillis() + ".jpg";
        String filePath = uploadPath + File.separator + fileName;

        // 写入文件到服务器文件系统
        Files.write(new File(filePath).toPath(), imageBytes);

        // 返回文件路径
        return fileName;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }
}
