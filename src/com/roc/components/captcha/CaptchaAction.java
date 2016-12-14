package com.roc.components.captcha;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.roc.core.base.BaseAction;
import org.apache.struts2.ServletActionContext;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * User: rocwu
 * Time: 2016-10-26
 * Time: 12:56
 * Desc: 随机二维码
 */
public class CaptchaAction extends BaseAction {

    private int v = 0;          //随机数
    private String captcha;     //用户填的验证码
    private String name;        //验证码字段名

    public String generate() throws Exception {
        ActionContext ac = ActionContext.getContext();
        HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);
        // set mime type as jpg image
        response.setContentType("image/jpg");

        BufferedImage image = new BufferedImage(180, 35, BufferedImage.TYPE_BYTE_INDEXED);

        Graphics2D graphics = image.createGraphics();

        // Set back ground of the generated image to white
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, 180, 35);

        // set gradient font of text to be converted to image
        GradientPaint gradientPaint = new GradientPaint(10, 5, Color.BLUE, 20, 10, Color.LIGHT_GRAY, true);
        graphics.setPaint(gradientPaint);
        Font font = new Font("Comic Sans MS", Font.BOLD, 30);
        graphics.setFont(font);

        // write string in the image
        String str = this.generateCaptchaString();
        graphics.drawString(str, 5, 30);

        request.getSession().setMaxInactiveInterval(5 * 60);
        request.getSession().setAttribute(this.name, str);

        // release resources used by graphics context
        graphics.dispose();

        try {
            ImageIO.write(image, "jpg", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String verify() throws Exception {
        //获取response、request对象
        ActionContext ac = ActionContext.getContext();
        HttpServletResponse response = (HttpServletResponse) ac.get(ServletActionContext.HTTP_RESPONSE);
        HttpServletRequest request = (HttpServletRequest) ac.get(ServletActionContext.HTTP_REQUEST);

        PrintWriter out = null;
        try {
            out = response.getWriter();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        String target = (String) request.getSession().getAttribute(this.name);
        boolean success = false;
        if (target != null && captcha != null &&
                target.toUpperCase().equals(captcha.toUpperCase()))
            success = true;
        String ret = "{\"success\":" + success + "}";
        out.print(ret); //返回ret信息
        out.flush();
        out.close();
        return null;
    }

    private String generateCaptchaString() {
        Random random = new Random(System.currentTimeMillis());
        int length = 7;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int baseCharNumber = Math.abs(random.nextInt()) % 62;
            int charNumber = 0;
            if (baseCharNumber < 26) {
                charNumber = 65 + baseCharNumber;
            }
            else if (baseCharNumber < 52){
                charNumber = 97 + (baseCharNumber - 26);
            }
            else {
                charNumber = 48 + (baseCharNumber - 52);
            }
            sb.append((char)charNumber);
        }

        return sb.toString();
    }

    public int getV() {
        return v;
    }

    public void setV(int v) {
        this.v = v;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
