package softech.apifacturacion.email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.*;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailSender {

    @Value("${spring.application.name}")
    String appName;
    
    String logoIcono = "https://593softech.com/LogoIcono.png";
  
    @Value("${email.config.host}")
    String host;
    
    @Value("${email.config.port}")
    String port;
    
    @Value("${email.config.username}")
    String username;
    
    @Value("${email.config.password}")
    String password;

    private JavaMailSender emailSender;

    public EmailSender() {
        // Configuración inicial del servidor SMTP
        emailSender = new JavaMailSenderImpl();
    }

    public void enviarCorreo(String emailDestino, String asunto, String contenido,
            String nombresDestinatario) {
        String str = "";
        str += "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
        str += "<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" style=\"font-family:'trebuchet ms', 'lucida grande', 'lucida sans unicode', 'lucida sans', tahoma, sans-serif\">";
        str += "";
        str += "<head>";
        str += "<meta charset=\"UTF-8\">";
        str += "<meta content=\"width=device-width, initial-scale=1\" name=\"viewport\">";
        str += "<meta name=\"x-apple-disable-message-reformatting\">";
        str += "<meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">";
        str += "<meta content=\"telephone=no\" name=\"format-detection\">";
        str += "<title>New email template 2023-09-29</title>";
        str += "<!--[if (mso 16)]><style type=\"text/css\"> a {text-decoration: none;} </style><![endif]-->";
        str += "<!--[if gte mso 9]><style>sup { font-size: 100% !important; }</style><![endif]-->";
        str += "<!--[if gte mso 9]><xml> <o:OfficeDocumentSettings> <o:AllowPNG></o:AllowPNG> <o:PixelsPerInch>96</o:PixelsPerInch> </o:OfficeDocumentSettings> </xml><![endif]-->";
        str += "<style type=\"text/css\">";
        str += ".section-title {";
        str += "padding: 5px 10px;";
        str += "background-color: #f6f6f6;";
        str += "border: 1px solid #dfdfdf;";
        str += "outline: 0;";
        str += "}";
        str += "";
        str += "#outlook a {";
        str += "padding: 0;";
        str += "}";
        str += "";
        str += ".es-button {";
        str += "mso-style-priority: 100!important;";
        str += "text-decoration: none!important;";
        str += "}";
        str += "";
        str += "a[x-apple-data-detectors] {";
        str += "color: inherit!important;";
        str += "text-decoration: none!important;";
        str += "font-size: inherit!important;";
        str += "font-family: inherit!important;";
        str += "font-weight: inherit!important;";
        str += "line-height: inherit!important;";
        str += "}";
        str += "";
        str += ".es-desk-hidden {";
        str += "display: none;";
        str += "float: left;";
        str += "overflow: hidden;";
        str += "width: 0;";
        str += "max-height: 0;";
        str += "line-height: 0;";
        str += "mso-hide: all;";
        str += "}";
        str += "";
        str += "@media only screen and (max-width:600px) {";
        str += "p,";
        str += "ul li,";
        str += "ol li,";
        str += "a {";
        str += "line-height: 150%!important";
        str += "}";
        str += "h1,";
        str += "h2,";
        str += "h3,";
        str += "h1 a,";
        str += "h2 a,";
        str += "h3 a {";
        str += "line-height: 120%";
        str += "}";
        str += "h1 {";
        str += "font-size: 30px!important;";
        str += "text-align: center";
        str += "}";
        str += "h2 {";
        str += "font-size: 26px!important;";
        str += "text-align: center";
        str += "}";
        str += "h3 {";
        str += "font-size: 20px!important;";
        str += "text-align: center";
        str += "}";
        str += ".es-header-body h1 a,";
        str += ".es-content-body h1 a,";
        str += ".es-footer-body h1 a {";
        str += "font-size: 30px!important";
        str += "}";
        str += ".es-header-body h2 a,";
        str += ".es-content-body h2 a,";
        str += ".es-footer-body h2 a {";
        str += "font-size: 26px!important";
        str += "}";
        str += ".es-header-body h3 a,";
        str += ".es-content-body h3 a,";
        str += ".es-footer-body h3 a {";
        str += "font-size: 20px!important";
        str += "}";
        str += ".es-menu td a {";
        str += "font-size: 12px!important";
        str += "}";
        str += ".es-header-body p,";
        str += ".es-header-body ul li,";
        str += ".es-header-body ol li,";
        str += ".es-header-body a {";
        str += "font-size: 16px!important";
        str += "}";
        str += ".es-content-body p,";
        str += ".es-content-body ul li,";
        str += ".es-content-body ol li,";
        str += ".es-content-body a {";
        str += "font-size: 16px!important";
        str += "}";
        str += ".es-footer-body p,";
        str += ".es-footer-body ul li,";
        str += ".es-footer-body ol li,";
        str += ".es-footer-body a {";
        str += "font-size: 16px!important";
        str += "}";
        str += ".es-infoblock p,";
        str += ".es-infoblock ul li,";
        str += ".es-infoblock ol li,";
        str += ".es-infoblock a {";
        str += "font-size: 12px!important";
        str += "}";
        str += "*[class=\"gmail-fix\"] {";
        str += "display: none!important";
        str += "}";
        str += ".es-m-txt-c,";
        str += ".es-m-txt-c h1,";
        str += ".es-m-txt-c h2,";
        str += ".es-m-txt-c h3 {";
        str += "text-align: center!important";
        str += "}";
        str += ".es-m-txt-r,";
        str += ".es-m-txt-r h1,";
        str += ".es-m-txt-r h2,";
        str += ".es-m-txt-r h3 {";
        str += "text-align: right!important";
        str += "}";
        str += ".es-m-txt-l,";
        str += ".es-m-txt-l h1,";
        str += ".es-m-txt-l h2,";
        str += ".es-m-txt-l h3 {";
        str += "text-align: left!important";
        str += "}";
        str += ".es-m-txt-r img,";
        str += ".es-m-txt-c img,";
        str += ".es-m-txt-l img {";
        str += "display: inline!important";
        str += "}";
        str += ".es-button-border {";
        str += "display: block!important";
        str += "}";
        str += "a.es-button,";
        str += "button.es-button {";
        str += "font-size: 20px!important;";
        str += "display: block!important;";
        str += "border-left-width: 0px!important;";
        str += "border-right-width: 0px!important";
        str += "}";
        str += ".es-adaptive table,";
        str += ".es-left,";
        str += ".es-right {";
        str += "width: 100%!important";
        str += "}";
        str += ".es-content table,";
        str += ".es-header table,";
        str += ".es-footer table,";
        str += ".es-content,";
        str += ".es-footer,";
        str += ".es-header {";
        str += "width: 100%!important;";
        str += "max-width: 600px!important";
        str += "}";
        str += ".es-adapt-td {";
        str += "display: block!important;";
        str += "width: 100%!important";
        str += "}";
        str += ".adapt-img {";
        str += "width: 100%!important;";
        str += "height: auto!important";
        str += "}";
        str += ".es-m-p0 {";
        str += "padding: 0!important";
        str += "}";
        str += ".es-m-p0r {";
        str += "padding-right: 0!important";
        str += "}";
        str += ".es-m-p0l {";
        str += "padding-left: 0!important";
        str += "}";
        str += ".es-m-p0t {";
        str += "padding-top: 0!important";
        str += "}";
        str += ".es-m-p0b {";
        str += "padding-bottom: 0!important";
        str += "}";
        str += ".es-m-p20b {";
        str += "padding-bottom: 20px!important";
        str += "}";
        str += ".es-mobile-hidden,";
        str += ".es-hidden {";
        str += "display: none!important";
        str += "}";
        str += "tr.es-desk-hidden,";
        str += "td.es-desk-hidden,";
        str += "table.es-desk-hidden {";
        str += "width: auto!important;";
        str += "overflow: visible!important;";
        str += "float: none!important;";
        str += "max-height: inherit!important;";
        str += "line-height: inherit!important";
        str += "}";
        str += "tr.es-desk-hidden {";
        str += "display: table-row!important";
        str += "}";
        str += "table.es-desk-hidden {";
        str += "display: table!important";
        str += "}";
        str += "td.es-desk-menu-hidden {";
        str += "display: table-cell!important";
        str += "}";
        str += ".es-menu td {";
        str += "width: 1%!important";
        str += "}";
        str += "table.es-table-not-adapt,";
        str += ".esd-block-html table {";
        str += "width: auto!important";
        str += "}";
        str += "table.es-social {";
        str += "display: inline-block!important";
        str += "}";
        str += "table.es-social td {";
        str += "display: inline-block!important";
        str += "}";
        str += ".es-desk-hidden {";
        str += "display: table-row!important;";
        str += "width: auto!important;";
        str += "overflow: visible!important;";
        str += "max-height: inherit!important";
        str += "}";
        str += ".h-auto {";
        str += "height: auto!important";
        str += "}";
        str += ".es-m-p5 {";
        str += "padding: 5px!important";
        str += "}";
        str += ".es-m-p5t {";
        str += "padding-top: 5px!important";
        str += "}";
        str += ".es-m-p5b {";
        str += "padding-bottom: 5px!important";
        str += "}";
        str += ".es-m-p5r {";
        str += "padding-right: 5px!important";
        str += "}";
        str += ".es-m-p5l {";
        str += "padding-left: 5px!important";
        str += "}";
        str += ".es-m-p10 {";
        str += "padding: 10px!important";
        str += "}";
        str += ".es-m-p10t {";
        str += "padding-top: 10px!important";
        str += "}";
        str += ".es-m-p10b {";
        str += "padding-bottom: 10px!important";
        str += "}";
        str += ".es-m-p10r {";
        str += "padding-right: 10px!important";
        str += "}";
        str += ".es-m-p10l {";
        str += "padding-left: 10px!important";
        str += "}";
        str += ".es-m-p15 {";
        str += "padding: 15px!important";
        str += "}";
        str += ".es-m-p15t {";
        str += "padding-top: 15px!important";
        str += "}";
        str += ".es-m-p15b {";
        str += "padding-bottom: 15px!important";
        str += "}";
        str += ".es-m-p15r {";
        str += "padding-right: 15px!important";
        str += "}";
        str += ".es-m-p15l {";
        str += "padding-left: 15px!important";
        str += "}";
        str += ".es-m-p20 {";
        str += "padding: 20px!important";
        str += "}";
        str += ".es-m-p20t {";
        str += "padding-top: 20px!important";
        str += "}";
        str += ".es-m-p20r {";
        str += "padding-right: 20px!important";
        str += "}";
        str += ".es-m-p20l {";
        str += "padding-left: 20px!important";
        str += "}";
        str += ".es-m-p25 {";
        str += "padding: 25px!important";
        str += "}";
        str += ".es-m-p25t {";
        str += "padding-top: 25px!important";
        str += "}";
        str += ".es-m-p25b {";
        str += "padding-bottom: 25px!important";
        str += "}";
        str += ".es-m-p25r {";
        str += "padding-right: 25px!important";
        str += "}";
        str += ".es-m-p25l {";
        str += "padding-left: 25px!important";
        str += "}";
        str += ".es-m-p30 {";
        str += "padding: 30px!important";
        str += "}";
        str += ".es-m-p30t {";
        str += "padding-top: 30px!important";
        str += "}";
        str += ".es-m-p30b {";
        str += "padding-bottom: 30px!important";
        str += "}";
        str += ".es-m-p30r {";
        str += "padding-right: 30px!important";
        str += "}";
        str += ".es-m-p30l {";
        str += "padding-left: 30px!important";
        str += "}";
        str += ".es-m-p35 {";
        str += "padding: 35px!important";
        str += "}";
        str += ".es-m-p35t {";
        str += "padding-top: 35px!important";
        str += "}";
        str += ".es-m-p35b {";
        str += "padding-bottom: 35px!important";
        str += "}";
        str += ".es-m-p35r {";
        str += "padding-right: 35px!important";
        str += "}";
        str += ".es-m-p35l {";
        str += "padding-left: 35px!important";
        str += "}";
        str += ".es-m-p40 {";
        str += "padding: 40px!important";
        str += "}";
        str += ".es-m-p40t {";
        str += "padding-top: 40px!important";
        str += "}";
        str += ".es-m-p40b {";
        str += "padding-bottom: 40px!important";
        str += "}";
        str += ".es-m-p40r {";
        str += "padding-right: 40px!important";
        str += "}";
        str += ".es-m-p40l {";
        str += "padding-left: 40px!important";
        str += "}";
        str += "}";
        str += "</style>";
        str += "</head>";
        str += "";
        str += "<body style=\"width:100%;font-family:'trebuchet ms', 'lucida grande', 'lucida sans unicode', 'lucida sans', tahoma, sans-serif;-webkit-text-size-adjust:100%;-ms-text-size-adjust:100%;padding:0;Margin:0\">";
        str += "<div class=\"es-wrapper-color\" style=\"background-color:#FFFFFF\">";
        str += "<!--[if gte mso 9]><v:background xmlns:v=\"urn:schemas-microsoft-com:vml\" fill=\"t\"> <v:fill type=\"tile\" color=\"#ffffff\"></v:fill> </v:background><![endif]-->";
        str += "<table class=\"es-wrapper\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;padding:0;Margin:0;width:100%;height:100%;background-repeat:repeat;background-position:center top;background-color:#FFFFFF\">";
        str += "<tr>";
        str += "<td valign=\"top\" style=\"padding:0;Margin:0\">";
        str += "<table class=\"es-content\" cellspacing=\"0\" cellpadding=\"0\" align=\"center\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;table-layout:fixed !important;width:100%\">";
        str += "<tr>";
        str += "<td align=\"center\" style=\"padding:0;Margin:0\">";
        str += "<table class=\"es-content-body\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-color:#ffffff;width:600px\" cellspacing=\"0\" cellpadding=\"0\" bgcolor=\"#ffffff\" align=\"center\">";
        str += "<tr>";
        str += "<td align=\"left\" style=\"Margin:0;padding-left:20px;padding-right:20px;padding-top:30px;padding-bottom:30px\">";
        str += "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">";
        str += "<tr>";
        str += "<td class=\"es-m-p0r\" valign=\"top\" align=\"center\" style=\"padding:0;Margin:0;width:560px\">";
        str += "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">";
        str += "<tr>";
        str += "<td align=\"center\" style=\"padding:0;Margin:0;padding-left:40px;padding-right:40px;font-size:0px\"><img src=\""
                + logoIcono
                + "\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" width=\"120\" height=\"120\"></td>";
        str += "</tr>";
        str += "<tr>";

        str += "</tr>";
        str += "<tr>";
        str += "<td align=\"center\" class=\"h-auto\" height=\"43\" style=\"padding:0;Margin:0;padding-top:10px;padding-bottom:10px\">";
        str += "<h2 style=\"Margin:0;line-height:23px;mso-line-height-rule:exactly;font-family:'trebuchet ms', 'lucida grande', 'lucida sans unicode', 'lucida sans', tahoma, sans-serif;font-size:23px;font-style:normal;font-weight:bold;color:#666666\">"
                + nombresDestinatario + "</h2>";
        str += "</td>";
        str += "</tr>";
        str += "<tr>";
        str += "<td align=\"center\" style=\"padding:0;Margin:0;padding-top:20px;padding-bottom:40px\">";
        str += "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:'trebuchet ms', 'lucida grande', 'lucida sans unicode', 'lucida sans', tahoma, sans-serif;line-height:21px;color:#666666;font-size:14px\">"
                + contenido + "</p>";
        str += "</td>";
        str += "</tr>";
        str += "<tr>";
        str += "<td align=\"center\" style=\"padding:0;Margin:0\">";
        str += "<p style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:'trebuchet ms', 'lucida grande', 'lucida sans unicode', 'lucida sans', tahoma, sans-serif;line-height:30px;color:#666666;font-size:20px\">"
                + "Atentamente" + "</p>";
        str += "</td>";
        str += "</tr>";
        str += "<tr>";
        str += "<td align=\"center\" style=\"padding:0;Margin:0\">";
        str += "<h3 style=\"Margin:0;-webkit-text-size-adjust:none;-ms-text-size-adjust:none;mso-line-height-rule:exactly;font-family:'trebuchet ms', 'lucida grande', 'lucida sans unicode', 'lucida sans', tahoma, sans-serif;line-height:33px;color:#666666;font-size:22px\"><strong>"
                + appName + "</strong></h3>";
        str += "</td>";
        str += "</tr>";
        str += "</table>";
        str += "</td>";
        str += "</tr>";
        str += "</table>";
        str += "</td>";
        str += "</tr>";
        str += "<tr>";
        str += "<td style=\"padding:0;Margin:0;padding-bottom:15px;padding-left:20px;padding-right:20px;background-color:#ffffff\" src=\"https://tlr.stripocdn.email/content/guids/CABINET_ff06f0f6afff91e4506ef568b46158c8/images/16221551886674353.jpg\" bgcolor=\"#ffffff\" align=\"left\">";
        str += "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">";
        str += "<tr>";
        str += "<td class=\"es-m-p20b\" align=\"left\" style=\"padding:0;Margin:0;width:560px\">";
        str += "<table style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px;background-position:left center\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\">";
        str += "<tr>";
        str += "<td align=\"center\" style=\"padding:0;Margin:0;padding-top:10px\">";
        str += "<h2 style=\"Margin:0;line-height:24px;mso-line-height-rule:exactly;font-family:'trebuchet ms', 'lucida grande', 'lucida sans unicode', 'lucida sans', tahoma, sans-serif;font-size:20px;font-style:normal;font-weight:bold;color:#090909\">Estamos disponibles en iOS y Android<br type=\"_moz\"></h2>";
        str += "</td>";
        str += "</tr>";
        str += "<tr>";
        str += "<td style=\"padding:0;Margin:0\">";
        str += "<table width=\"100%\" cellspacing=\"0\" cellpadding=\"0\" role=\"presentation\" style=\"mso-table-lspace:0pt;mso-table-rspace:0pt;border-collapse:collapse;border-spacing:0px\">";
        str += "<tr>";
        str += "<td align=\"right\" style=\"padding:0;Margin:0;padding-top:5px;padding-bottom:5px\"><img class=\"adapt-img\" src=\"https://mpddfc.stripocdn.email/content/guids/CABINET_6839a5bce39b18d51ded21ca93c13ed9/images/3741557735392877.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" width=\"150\" height=\"57\"></td>";
        str += "<td align=\"left\" style=\"padding:0;Margin:0;padding-top:5px;padding-bottom:5px\"><img class=\"adapt-img\" src=\"https://mpddfc.stripocdn.email/content/guids/CABINET_6839a5bce39b18d51ded21ca93c13ed9/images/65121557735423574.png\" alt style=\"display:block;border:0;outline:none;text-decoration:none;-ms-interpolation-mode:bicubic\" width=\"150\" height=\"56\"></td>";
        str += "</tr>";
        str += "</table>";
        str += "</td>";
        str += "</tr>";
        str += "</table>";
        str += "</td>";
        str += "</tr>";
        str += "</table>";
        str += "</td>";
        str += "</tr>";
        str += "</table>";
        str += "</td>";
        str += "</tr>";
        str += "</table>";
        str += "</td>";
        str += "</tr>";
        str += "</table>";
        str += "</div>";
        str += "</body>";
        str += "";
        str += "</html>";

        // Configurar la configuración del servidor SMTP
        JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) emailSender;
        mailSenderImpl.setHost(host);
        mailSenderImpl.setPort(Integer.parseInt(port));
        mailSenderImpl.setUsername(username);
        mailSenderImpl.setPassword(password);

        // Habilitar el uso de TLS para la conexión SMTP
        mailSenderImpl.getJavaMailProperties().setProperty("mail.smtp.starttls.enable", "true");

        // Configurar el dominio EHLO/HELO en blanco
        mailSenderImpl.getJavaMailProperties().setProperty("mail.smtp.localhost", "");

        // Crear un mensaje de correo electrónico con contenido HTML
        MimeMessage mensaje = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, "utf-8");

        try {
            helper.setFrom(username);
            helper.setTo(emailDestino);
            helper.setSubject(asunto);
            helper.setText(str, true); // Habilitar contenido HTML

            // Enviar el correo electrónico
            emailSender.send(mensaje);
            
        } catch (MessagingException e) {
            // Manejar excepciones de envío de correo aquí
            e.printStackTrace();
        }
    }

}
