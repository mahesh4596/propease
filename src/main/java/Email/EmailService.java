package Email;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailService {

    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    private static final String EMAIL = System.getenv("EMAIL_USER");
    private static final String APP_PASSWORD = System.getenv("EMAIL_PASSWORD");

    public static boolean sendEmail(String to, String subject, String htmlMessage) {
        try {

            Properties properties = new Properties();

            properties.put("mail.smtp.auth", "true");

            properties.put("mail.smtp.starttls.enable", "true");

            properties.put("mail.smtp.host", SMTP_HOST);

            properties.put("mail.smtp.port", SMTP_PORT);

            Session session = Session.getInstance(
                    properties,
                    new Authenticator() {

                        @Override
                        protected PasswordAuthentication
                        getPasswordAuthentication() {
                            return new PasswordAuthentication(EMAIL, APP_PASSWORD);
                        }
                    }
            );

            Message message = new MimeMessage(session);

            message.setFrom(new InternetAddress(EMAIL, "PropEase"));

            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(to)
            );

            message.setSubject(subject);

            message.setContent(
                    htmlMessage,
                    "text/html; charset=UTF-8"
            );

            Transport.send(message);

            System.out.println("Beautiful email sent successfully to: " + to);

            return true;

        }
        catch (Exception e) {
            System.out.println("Email sending failed.");
            e.printStackTrace();

            return false;
        }
    }

    public static boolean sendCustomerWelcomeEmail(String to, String name, String mobile, String city, String customerType) {

        String html = """
            <!DOCTYPE html>
            <html>

            <head>
                <meta charset="UTF-8">
                <meta name="viewport"
                      content="width=device-width, initial-scale=1.0">
                <title>Welcome to PropEase</title>
            </head>

            <body style="
                margin:0;
                padding:0;
                background:#f1f5f9;
                font-family:Arial,Helvetica,sans-serif;
            ">

            <table width="100%" cellpadding="0" cellspacing="0"
                   style="background:#f1f5f9;padding:30px 10px;">

                <tr>
                    <td align="center">

                        <table width="600"
                               cellpadding="0"
                               cellspacing="0"
                               style="
                                   max-width:600px;
                                   width:100%;
                                   background:#ffffff;
                                   border-radius:18px;
                                   overflow:hidden;
                                   box-shadow:0 8px 30px rgba(0,0,0,0.08);
                               ">

                            <!-- HEADER -->

                            <tr>
                                <td style="
                                    background:linear-gradient(
                                        135deg,
                                        #0d47a1,
                                        #1976d2,
                                        #42a5f5
                                    );
                                    padding:35px 30px;
                                    text-align:center;
                                ">

                                    <div style="
                                        font-size:42px;
                                        margin-bottom:10px;
                                    ">
                                        🏡
                                    </div>

                                    <h1 style="
                                        margin:0;
                                        color:#ffffff;
                                        font-size:30px;
                                    ">
                                        PropEase
                                    </h1>

                                    <p style="
                                        margin:8px 0 0;
                                        color:#dbeafe;
                                        font-size:14px;
                                    ">
                                        Smart Property Management
                                    </p>

                                </td>
                            </tr>


                            <!-- CONTENT -->

                            <tr>
                                <td style="padding:35px 40px;">

                                    <h2 style="
                                        margin:0 0 10px;
                                        color:#172554;
                                        font-size:24px;
                                    ">
                                        Welcome, {{NAME}}! 👋
                                    </h2>

                                    <p style="
                                        margin:0 0 25px;
                                        color:#64748b;
                                        font-size:15px;
                                        line-height:1.7;
                                    ">
                                        We're happy to have you registered
                                        with <strong>PropEase</strong>.
                                        Your customer profile has been
                                        successfully created.
                                    </p>


                                    <!-- SUCCESS -->

                                    <table width="100%"
                                           cellpadding="0"
                                           cellspacing="0">

                                        <tr>

                                            <td style="
                                                background:#ecfdf5;
                                                border:1px solid #a7f3d0;
                                                border-radius:12px;
                                                padding:16px;
                                            ">

                                                <span style="
                                                    color:#047857;
                                                    font-size:14px;
                                                    font-weight:bold;
                                                ">
                                                    ✓ Registration Successful
                                                </span>

                                            </td>

                                        </tr>

                                    </table>


                                    <div style="height:20px;"></div>


                                    <!-- CUSTOMER DETAILS -->

                                    <table width="100%"
                                           cellpadding="0"
                                           cellspacing="0"
                                           style="
                                               border:1px solid #e2e8f0;
                                               border-radius:14px;
                                               overflow:hidden;
                                           ">

                                        <tr>

                                            <td colspan="2"
                                                style="
                                                    background:#f8fafc;
                                                    padding:15px 18px;
                                                    color:#0f172a;
                                                    font-weight:bold;
                                                    font-size:15px;
                                                ">

                                                👤 Customer Details

                                            </td>

                                        </tr>


                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                Name
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#0f172a;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{NAME}}
                                            </td>

                                        </tr>


                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                Mobile
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#0f172a;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{MOBILE}}
                                            </td>

                                        </tr>


                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                City
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#0f172a;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{CITY}}
                                            </td>

                                        </tr>


                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                Customer Type
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#1976d2;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{TYPE}}
                                            </td>

                                        </tr>

                                    </table>


                                    <div style="height:25px;"></div>


                                    <p style="
                                        color:#64748b;
                                        font-size:14px;
                                        line-height:1.7;
                                        margin:0;
                                    ">
                                        You can now use our services for
                                        managing properties, finding
                                        properties and handling real
                                        estate deals.
                                    </p>


                                    <div style="height:25px;"></div>


                                    <p style="
                                        margin:0;
                                        color:#334155;
                                        font-size:14px;
                                    ">
                                        Thank you for choosing
                                        <strong style="color:#1976d2;">
                                            PropEase
                                        </strong>.
                                    </p>

                                </td>
                            </tr>


                            <!-- FOOTER -->

                            <tr>

                                <td style="
                                    background:#0f172a;
                                    padding:25px 30px;
                                    text-align:center;
                                ">

                                    <p style="
                                        margin:0 0 8px;
                                        color:#ffffff;
                                        font-size:15px;
                                        font-weight:bold;
                                    ">
                                        PropEase
                                    </p>

                                    <p style="
                                        margin:0;
                                        color:#94a3b8;
                                        font-size:12px;
                                        line-height:1.6;
                                    ">
                                        Smart Property Management System
                                        <br>
                                        This is an automated email.
                                        Please do not reply.
                                    </p>

                                </td>

                            </tr>

                        </table>

                    </td>
                </tr>

            </table>

            </body>
            </html>
            """;


        // =========================================================
        // INSERT CUSTOMER DATA
        // =========================================================

        html = html.replace(
                "{{NAME}}",
                escapeHtml(name)
        );

        html = html.replace(
                "{{MOBILE}}",
                escapeHtml(mobile)
        );

        html = html.replace(
                "{{CITY}}",
                escapeHtml(city)
        );

        html = html.replace(
                "{{TYPE}}",
                escapeHtml(customerType)
        );


        return sendEmail(
                to,
                "Welcome to PropEase 🏡",
                html
        );
    }

    public static boolean sendCustomerUpdateEmail(String to, String name, String mobile, String city, String customerType) {

        String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                </head>
        
                <body style="
                    margin:0;
                    padding:0;
                    background:#f1f5f9;
                    font-family:Arial,Helvetica,sans-serif;
                ">
        
                <table width="100%" cellpadding="0" cellspacing="0"
                       style="background:#f1f5f9;padding:30px 10px;">
        
                    <tr>
                        <td align="center">
        
                            <table width="600"
                                   cellpadding="0"
                                   cellspacing="0"
                                   style="
                                       max-width:600px;
                                       width:100%;
                                       background:#ffffff;
                                       border-radius:18px;
                                       overflow:hidden;
                                       box-shadow:0 8px 30px rgba(0,0,0,0.08);
                                   ">
        
                                <!-- HEADER -->
        
                                <tr>
                                    <td style="
                                        background:linear-gradient(
                                            135deg,
                                            #0d47a1,
                                            #1976d2,
                                            #42a5f5
                                        );
                                        padding:35px 30px;
                                        text-align:center;
                                    ">
        
                                        <div style="font-size:42px;">
                                            ✨
                                        </div>
        
                                        <h1 style="
                                            margin:8px 0;
                                            color:white;
                                            font-size:30px;
                                        ">
                                            PropEase
                                        </h1>
        
                                        <p style="
                                            margin:0;
                                            color:#dbeafe;
                                            font-size:14px;
                                        ">
                                            Smart Property Management
                                        </p>
        
                                    </td>
                                </tr>
        
        
                                <!-- CONTENT -->
        
                                <tr>
                                    <td style="padding:35px 40px;">
        
                                        <h2 style="
                                            margin:0 0 12px;
                                            color:#172554;
                                            font-size:24px;
                                        ">
                                            Your Profile Has Been Updated ✨
                                        </h2>
        
                                        <p style="
                                            color:#64748b;
                                            font-size:15px;
                                            line-height:1.7;
                                        ">
                                            Dear <strong>{{NAME}}</strong>,
                                        </p>
        
                                        <p style="
                                            color:#64748b;
                                            font-size:14px;
                                            line-height:1.7;
                                        ">
                                            Your customer profile on
                                            <strong>PropEase</strong> has been
                                            successfully updated.
                                        </p>
        
        
                                        <table width="100%"
                                               cellpadding="0"
                                               cellspacing="0"
                                               style="
                                                   margin-top:25px;
                                                   border:1px solid #e2e8f0;
                                                   border-radius:14px;
                                                   overflow:hidden;
                                               ">
        
                                            <tr>
                                                <td colspan="2"
                                                    style="
                                                        padding:16px;
                                                        background:#f8fafc;
                                                        font-weight:bold;
                                                        color:#0f172a;
                                                    ">
                                                    👤 Updated Customer Details
                                                </td>
                                            </tr>
        
                                            <tr>
                                                <td style="
                                                    padding:12px 18px;
                                                    color:#64748b;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    Name
                                                </td>
        
                                                <td style="
                                                    padding:12px 18px;
                                                    text-align:right;
                                                    font-weight:bold;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    {{NAME}}
                                                </td>
                                            </tr>
        
                                            <tr>
                                                <td style="
                                                    padding:12px 18px;
                                                    color:#64748b;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    Mobile
                                                </td>
        
                                                <td style="
                                                    padding:12px 18px;
                                                    text-align:right;
                                                    font-weight:bold;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    {{MOBILE}}
                                                </td>
                                            </tr>
        
                                            <tr>
                                                <td style="
                                                    padding:12px 18px;
                                                    color:#64748b;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    City
                                                </td>
        
                                                <td style="
                                                    padding:12px 18px;
                                                    text-align:right;
                                                    font-weight:bold;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    {{CITY}}
                                                </td>
                                            </tr>
        
                                            <tr>
                                                <td style="
                                                    padding:12px 18px;
                                                    color:#64748b;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    Customer Type
                                                </td>
        
                                                <td style="
                                                    padding:12px 18px;
                                                    text-align:right;
                                                    color:#1976d2;
                                                    font-weight:bold;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    {{TYPE}}
                                                </td>
                                            </tr>
        
                                        </table>
        
        
                                        <div style="
                                            margin-top:25px;
                                            padding:16px;
                                            background:#ecfdf5;
                                            border:1px solid #a7f3d0;
                                            border-radius:12px;
                                            color:#047857;
                                            font-weight:bold;
                                            font-size:14px;
                                        ">
                                            ✓ Profile update completed successfully
                                        </div>
        
        
                                        <p style="
                                            margin-top:25px;
                                            color:#64748b;
                                            font-size:14px;
                                            line-height:1.7;
                                        ">
                                            If you did not request this update,
                                            please contact the PropEase administrator.
                                        </p>
        
                                    </td>
                                </tr>
        
        
                                <!-- FOOTER -->
        
                                <tr>
                                    <td style="
                                        background:#0f172a;
                                        padding:25px;
                                        text-align:center;
                                    ">
        
                                        <strong style="color:white;">
                                            PropEase
                                        </strong>
        
                                        <p style="
                                            margin:8px 0 0;
                                            color:#94a3b8;
                                            font-size:12px;
                                        ">
                                            Smart Property Management System<br>
                                            This is an automated email.
                                        </p>
        
                                    </td>
                                </tr>
        
                            </table>
        
                        </td>
                    </tr>
        
                </table>
        
                </body>
                </html>
                """
                    .replace("{{NAME}}", escapeHtml(name))
                    .replace("{{MOBILE}}", escapeHtml(mobile))
                    .replace("{{CITY}}", escapeHtml(city))
                    .replace("{{TYPE}}", escapeHtml(customerType));

        return sendEmail(
                to,
                "Your PropEase Profile Has Been Updated ✨",
                html
        );
    }

    public static boolean sendPropertyListingEmail(String to, String customerName, String address, String area, String city, String size, String propertyType, String structureType, String front, String rear, String left, String right, String direction, String totalPrice) {

        String html = """

            <!DOCTYPE html>

            <html>

            <head>

                <meta charset="UTF-8">

                <meta name="viewport"
                      content="width=device-width,
                               initial-scale=1.0">

                <title>Property Listed - PropEase</title>

            </head>


            <body style="
                margin:0;
                padding:0;
                background:#f1f5f9;
                font-family:Arial,Helvetica,sans-serif;
            ">


            <!-- MAIN BACKGROUND -->

            <table width="100%"
                   cellpadding="0"
                   cellspacing="0"
                   style="
                       background:#f1f5f9;
                       padding:30px 10px;
                   ">

                <tr>

                    <td align="center">


                        <!-- EMAIL CARD -->

                        <table width="620"
                               cellpadding="0"
                               cellspacing="0"
                               style="
                                   max-width:620px;
                                   width:100%;
                                   background:#ffffff;
                                   border-radius:20px;
                                   overflow:hidden;
                                   box-shadow:
                                   0 8px 30px
                                   rgba(0,0,0,0.08);
                               ">


                            <!-- ================================= -->
                            <!-- HEADER                            -->
                            <!-- ================================= -->

                            <tr>

                                <td style="
                                    background:linear-gradient(
                                        135deg,
                                        #0d47a1,
                                        #1976d2,
                                        #42a5f5
                                    );
                                    padding:35px 30px;
                                    text-align:center;
                                ">

                                    <div style="
                                        font-size:44px;
                                        margin-bottom:8px;
                                    ">
                                        🏡
                                    </div>


                                    <h1 style="
                                        margin:0;
                                        color:#ffffff;
                                        font-size:30px;
                                        letter-spacing:0.5px;
                                    ">
                                        PropEase
                                    </h1>


                                    <p style="
                                        margin:8px 0 0;
                                        color:#dbeafe;
                                        font-size:14px;
                                    ">
                                        Smart Property Management
                                    </p>

                                </td>

                            </tr>


                            <!-- ================================= -->
                            <!-- TITLE                              -->
                            <!-- ================================= -->

                            <tr>

                                <td style="
                                    padding:35px 40px 10px;
                                ">

                                    <h2 style="
                                        margin:0;
                                        color:#172554;
                                        font-size:24px;
                                    ">
                                        Property Listing Confirmed 🎉
                                    </h2>


                                    <p style="
                                        margin:10px 0 0;
                                        color:#64748b;
                                        font-size:15px;
                                        line-height:1.7;
                                    ">
                                        Hello <strong>{{CUSTOMER_NAME}}</strong>,
                                    </p>


                                    <p style="
                                        margin:8px 0 0;
                                        color:#64748b;
                                        font-size:14px;
                                        line-height:1.7;
                                    ">
                                        Your property has been successfully
                                        listed in the PropEase property
                                        management system.
                                    </p>

                                </td>

                            </tr>


                            <!-- ================================= -->
                            <!-- SUCCESS BADGE                      -->
                            <!-- ================================= -->

                            <tr>

                                <td style="
                                    padding:20px 40px;
                                ">

                                    <table width="100%"
                                           cellpadding="0"
                                           cellspacing="0">

                                        <tr>

                                            <td style="
                                                background:#ecfdf5;
                                                border:1px solid #a7f3d0;
                                                border-radius:12px;
                                                padding:16px 18px;
                                            ">

                                                <span style="
                                                    color:#047857;
                                                    font-size:14px;
                                                    font-weight:bold;
                                                ">
                                                    ✓ PROPERTY LISTED
                                                    SUCCESSFULLY
                                                </span>

                                            </td>

                                        </tr>

                                    </table>

                                </td>

                            </tr>


                            <!-- ================================= -->
                            <!-- PROPERTY DETAILS                  -->
                            <!-- ================================= -->

                            <tr>

                                <td style="
                                    padding:0 40px 30px;
                                ">

                                    <table width="100%"
                                           cellpadding="0"
                                           cellspacing="0"
                                           style="
                                               border:1px solid #e2e8f0;
                                               border-radius:14px;
                                               overflow:hidden;
                                           ">


                                        <!-- CARD TITLE -->

                                        <tr>

                                            <td colspan="2"
                                                style="
                                                    background:#f8fafc;
                                                    padding:16px 18px;
                                                    color:#0f172a;
                                                    font-size:16px;
                                                    font-weight:bold;
                                                ">

                                                🏠 Property Details

                                            </td>

                                        </tr>


                                        <!-- ADDRESS -->

                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                Location / Address
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#0f172a;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{ADDRESS}}
                                            </td>

                                        </tr>


                                        <!-- AREA -->

                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                Area
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#0f172a;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{AREA}}
                                            </td>

                                        </tr>


                                        <!-- CITY -->

                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                City
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#0f172a;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{CITY}}
                                            </td>

                                        </tr>


                                        <!-- SIZE -->

                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                Size
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#0f172a;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{SIZE}} Sq.Yd
                                            </td>

                                        </tr>


                                        <!-- PROPERTY TYPE -->

                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                Property Type
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#1976d2;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{PROPERTY_TYPE}}
                                            </td>

                                        </tr>


                                        <!-- STRUCTURE -->

                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                Structure
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#0f172a;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{STRUCTURE}}
                                            </td>

                                        </tr>


                                        <!-- DIRECTION -->

                                        <tr>

                                            <td style="
                                                padding:12px 18px;
                                                color:#64748b;
                                                font-size:14px;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                Direction
                                            </td>

                                            <td style="
                                                padding:12px 18px;
                                                color:#0f172a;
                                                font-size:14px;
                                                font-weight:bold;
                                                text-align:right;
                                                border-top:1px solid #e2e8f0;
                                            ">
                                                {{DIRECTION}}
                                            </td>

                                        </tr>

                                    </table>

                                </td>

                            </tr>


                            <!-- ================================= -->
                            <!-- DIMENSIONS                         -->
                            <!-- ================================= -->

                            <tr>

                                <td style="
                                    padding:0 40px 30px;
                                ">

                                    <table width="100%"
                                           cellpadding="0"
                                           cellspacing="0"
                                           style="
                                               border:1px solid #dbeafe;
                                               border-radius:14px;
                                               background:#f8fbff;
                                               overflow:hidden;
                                           ">

                                        <tr>

                                            <td colspan="4"
                                                style="
                                                    padding:16px 18px;
                                                    color:#0d47a1;
                                                    font-size:16px;
                                                    font-weight:bold;
                                                ">

                                                📐 Property Dimensions

                                            </td>

                                        </tr>


                                        <tr>

                                            <td align="center"
                                                style="padding:15px 5px;">

                                                <div style="
                                                    color:#64748b;
                                                    font-size:12px;
                                                ">
                                                    FRONT
                                                </div>

                                                <div style="
                                                    margin-top:5px;
                                                    color:#0f172a;
                                                    font-size:15px;
                                                    font-weight:bold;
                                                ">
                                                    {{FRONT}}
                                                </div>

                                            </td>


                                            <td align="center"
                                                style="padding:15px 5px;">

                                                <div style="
                                                    color:#64748b;
                                                    font-size:12px;
                                                ">
                                                    REAR
                                                </div>

                                                <div style="
                                                    margin-top:5px;
                                                    color:#0f172a;
                                                    font-size:15px;
                                                    font-weight:bold;
                                                ">
                                                    {{REAR}}
                                                </div>

                                            </td>


                                            <td align="center"
                                                style="padding:15px 5px;">

                                                <div style="
                                                    color:#64748b;
                                                    font-size:12px;
                                                ">
                                                    LEFT
                                                </div>

                                                <div style="
                                                    margin-top:5px;
                                                    color:#0f172a;
                                                    font-size:15px;
                                                    font-weight:bold;
                                                ">
                                                    {{LEFT}}
                                                </div>

                                            </td>


                                            <td align="center"
                                                style="padding:15px 5px;">

                                                <div style="
                                                    color:#64748b;
                                                    font-size:12px;
                                                ">
                                                    RIGHT
                                                </div>

                                                <div style="
                                                    margin-top:5px;
                                                    color:#0f172a;
                                                    font-size:15px;
                                                    font-weight:bold;
                                                ">
                                                    {{RIGHT}}
                                                </div>

                                            </td>

                                        </tr>

                                    </table>

                                </td>

                            </tr>


                            <!-- ================================= -->
                            <!-- PRICE                              -->
                            <!-- ================================= -->

                            <tr>

                                <td style="
                                    padding:0 40px 30px;
                                ">

                                    <table width="100%"
                                           cellpadding="0"
                                           cellspacing="0"
                                           style="
                                               background:#eff6ff;
                                               border:1px solid #bfdbfe;
                                               border-radius:14px;
                                           ">

                                        <tr>

                                            <td style="
                                                padding:20px;
                                                text-align:center;
                                            ">

                                                <div style="
                                                    color:#64748b;
                                                    font-size:13px;
                                                    margin-bottom:6px;
                                                ">
                                                    TOTAL PRICE DEMANDED
                                                </div>

                                                <div style="
                                                    color:#0d47a1;
                                                    font-size:27px;
                                                    font-weight:bold;
                                                ">
                                                    ₹ {{PRICE}}
                                                </div>

                                            </td>

                                        </tr>

                                    </table>

                                </td>

                            </tr>


                            <!-- ================================= -->
                            <!-- MESSAGE                            -->
                            <!-- ================================= -->

                            <tr>

                                <td style="
                                    padding:0 40px 35px;
                                ">

                                    <p style="
                                        margin:0;
                                        color:#64748b;
                                        font-size:14px;
                                        line-height:1.7;
                                    ">
                                        Your property information has been
                                        securely recorded in PropEase.
                                        You can continue managing your
                                        property through the PropEase
                                        system.
                                    </p>

                                    <div style="height:20px;"></div>

                                    <p style="
                                        margin:0;
                                        color:#334155;
                                        font-size:14px;
                                    ">
                                        Thank you for choosing
                                        <strong style="color:#1976d2;">
                                            PropEase
                                        </strong>.
                                    </p>

                                </td>

                            </tr>


                            <!-- ================================= -->
                            <!-- FOOTER                             -->
                            <!-- ================================= -->

                            <tr>

                                <td style="
                                    background:#0f172a;
                                    padding:25px 30px;
                                    text-align:center;
                                ">

                                    <p style="
                                        margin:0 0 8px;
                                        color:#ffffff;
                                        font-size:15px;
                                        font-weight:bold;
                                    ">
                                        PropEase
                                    </p>

                                    <p style="
                                        margin:0;
                                        color:#94a3b8;
                                        font-size:12px;
                                        line-height:1.6;
                                    ">
                                        Smart Property Management System
                                        <br>
                                        This is an automated email.
                                        Please do not reply.
                                    </p>

                                </td>

                            </tr>


                        </table>

                    </td>

                </tr>

            </table>

            </body>

            </html>

            """;


        // =========================================================
        // INSERT DATA
        // =========================================================

        html = html.replace(
                "{{CUSTOMER_NAME}}",
                escapeHtml(customerName)
        );

        html = html.replace(
                "{{ADDRESS}}",
                escapeHtml(address)
        );

        html = html.replace(
                "{{AREA}}",
                escapeHtml(area)
        );

        html = html.replace(
                "{{CITY}}",
                escapeHtml(city)
        );

        html = html.replace(
                "{{SIZE}}",
                escapeHtml(size)
        );

        html = html.replace(
                "{{PROPERTY_TYPE}}",
                escapeHtml(propertyType)
        );

        html = html.replace(
                "{{STRUCTURE}}",
                escapeHtml(structureType)
        );

        html = html.replace(
                "{{FRONT}}",
                escapeHtml(front)
        );

        html = html.replace(
                "{{REAR}}",
                escapeHtml(rear)
        );

        html = html.replace(
                "{{LEFT}}",
                escapeHtml(left)
        );

        html = html.replace(
                "{{RIGHT}}",
                escapeHtml(right)
        );

        html = html.replace(
                "{{DIRECTION}}",
                escapeHtml(direction)
        );

        html = html.replace(
                "{{PRICE}}",
                escapeHtml(totalPrice)
        );


        return sendEmail(
                to,
                "Property Listing Confirmed - PropEase 🏡",
                html
        );
    }

    public static boolean sendPropertyStatusEmail(String to, String customerName, String address, String area, String city, String size, String propertyType, String price, String action) {

        boolean updated = action.equalsIgnoreCase("UPDATED");

        String title = updated
                ? "Your Property Listing Has Been Updated ✨"
                : "Your Property Listing Has Been Removed";

        String subject = updated
                ? "PropEase - Property Listing Updated ✨"
                : "PropEase - Property Listing Removed";

        String statusText = updated
                ? "✓ PROPERTY UPDATED SUCCESSFULLY"
                : "✓ PROPERTY REMOVED";

        String statusBackground = updated
                ? "#ecfdf5"
                : "#fef2f2";

        String statusBorder = updated
                ? "#a7f3d0"
                : "#fecaca";

        String statusColor = updated
                ? "#047857"
                : "#b91c1c";

        String html = """
                <!DOCTYPE html>
                <html>
        
                <head>
                    <meta charset="UTF-8">
                </head>
        
                <body style="
                    margin:0;
                    padding:0;
                    background:#f1f5f9;
                    font-family:Arial,Helvetica,sans-serif;
                ">
        
                <table width="100%" cellpadding="0" cellspacing="0"
                       style="background:#f1f5f9;padding:30px 10px;">
        
                    <tr>
                        <td align="center">
        
                            <table width="620"
                                   cellpadding="0"
                                   cellspacing="0"
                                   style="
                                       max-width:620px;
                                       width:100%;
                                       background:#ffffff;
                                       border-radius:20px;
                                       overflow:hidden;
                                       box-shadow:0 8px 30px rgba(0,0,0,0.08);
                                   ">
        
                                <!-- HEADER -->
        
                                <tr>
                                    <td style="
                                        background:linear-gradient(
                                            135deg,
                                            #0d47a1,
                                            #1976d2,
                                            #42a5f5
                                        );
                                        padding:35px;
                                        text-align:center;
                                    ">
        
                                        <div style="
                                            font-size:44px;
                                        ">
                                            🏡
                                        </div>
        
                                        <h1 style="
                                            margin:8px 0;
                                            color:white;
                                            font-size:30px;
                                        ">
                                            PropEase
                                        </h1>
        
                                        <p style="
                                            margin:0;
                                            color:#dbeafe;
                                            font-size:14px;
                                        ">
                                            Smart Property Management
                                        </p>
        
                                    </td>
                                </tr>
        
        
                                <!-- CONTENT -->
        
                                <tr>
                                    <td style="padding:35px 40px;">
        
                                        <h2 style="
                                            margin:0 0 12px;
                                            color:#172554;
                                            font-size:24px;
                                        ">
                                            {{TITLE}}
                                        </h2>
        
                                        <p style="
                                            color:#64748b;
                                            font-size:15px;
                                            line-height:1.7;
                                        ">
                                            Dear <strong>{{NAME}}</strong>,
                                        </p>
        
                                        <p style="
                                            color:#64748b;
                                            font-size:14px;
                                            line-height:1.7;
                                        ">
                                            Your property listing at
                                            <strong>{{ADDRESS}}</strong>
                                            has been {{ACTION_TEXT}}
                                            in the PropEase system.
                                        </p>
        
        
                                        <div style="
                                            margin:25px 0;
                                            padding:16px;
                                            background:{{STATUS_BACKGROUND}};
                                            border:1px solid {{STATUS_BORDER}};
                                            border-radius:12px;
                                            color:{{STATUS_COLOR}};
                                            font-weight:bold;
                                        ">
                                            {{STATUS_TEXT}}
                                        </div>
        
        
                                        <!-- PROPERTY DETAILS -->
        
                                        <table width="100%"
                                               cellpadding="0"
                                               cellspacing="0"
                                               style="
                                                   border:1px solid #e2e8f0;
                                                   border-radius:14px;
                                                   overflow:hidden;
                                               ">
        
                                            <tr>
                                                <td colspan="2"
                                                    style="
                                                        background:#f8fafc;
                                                        padding:16px;
                                                        font-weight:bold;
                                                        color:#0f172a;
                                                    ">
                                                    🏠 Property Details
                                                </td>
                                            </tr>
        
                                            <tr>
                                                <td style="
                                                    padding:12px 18px;
                                                    color:#64748b;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    Address
                                                </td>
        
                                                <td style="
                                                    padding:12px 18px;
                                                    text-align:right;
                                                    font-weight:bold;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    {{ADDRESS}}
                                                </td>
                                            </tr>
        
                                            <tr>
                                                <td style="
                                                    padding:12px 18px;
                                                    color:#64748b;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    Area
                                                </td>
        
                                                <td style="
                                                    padding:12px 18px;
                                                    text-align:right;
                                                    font-weight:bold;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    {{AREA}}
                                                </td>
                                            </tr>
        
                                            <tr>
                                                <td style="
                                                    padding:12px 18px;
                                                    color:#64748b;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    City
                                                </td>
        
                                                <td style="
                                                    padding:12px 18px;
                                                    text-align:right;
                                                    font-weight:bold;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    {{CITY}}
                                                </td>
                                            </tr>
        
                                            <tr>
                                                <td style="
                                                    padding:12px 18px;
                                                    color:#64748b;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    Size
                                                </td>
        
                                                <td style="
                                                    padding:12px 18px;
                                                    text-align:right;
                                                    font-weight:bold;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    {{SIZE}} Sq.Yd
                                                </td>
                                            </tr>
        
                                            <tr>
                                                <td style="
                                                    padding:12px 18px;
                                                    color:#64748b;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    Property Type
                                                </td>
        
                                                <td style="
                                                    padding:12px 18px;
                                                    text-align:right;
                                                    color:#1976d2;
                                                    font-weight:bold;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    {{PROPERTY_TYPE}}
                                                </td>
                                            </tr>
        
                                            <tr>
                                                <td style="
                                                    padding:12px 18px;
                                                    color:#64748b;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    Price
                                                </td>
        
                                                <td style="
                                                    padding:12px 18px;
                                                    text-align:right;
                                                    color:#0d47a1;
                                                    font-weight:bold;
                                                    border-top:1px solid #e2e8f0;
                                                ">
                                                    ₹{{PRICE}}
                                                </td>
                                            </tr>
        
                                        </table>
        
        
                                        <p style="
                                            margin-top:25px;
                                            color:#64748b;
                                            font-size:14px;
                                            line-height:1.7;
                                        ">
                                            Thank you for using
                                            <strong style="color:#1976d2;">
                                                PropEase
                                            </strong>.
                                        </p>
        
                                    </td>
                                </tr>
        
        
                                <!-- FOOTER -->
        
                                <tr>
                                    <td style="
                                        background:#0f172a;
                                        padding:25px;
                                        text-align:center;
                                    ">
        
                                        <strong style="color:white;">
                                            PropEase
                                        </strong>
        
                                        <p style="
                                            margin:8px 0 0;
                                            color:#94a3b8;
                                            font-size:12px;
                                        ">
                                            Smart Property Management System<br>
                                            This is an automated email.
                                        </p>
        
                                    </td>
                                </tr>
        
                            </table>
        
                        </td>
                    </tr>
        
                </table>
        
                </body>
                </html>
                """
                    .replace("{{TITLE}}", escapeHtml(title))
                    .replace("{{NAME}}", escapeHtml(customerName))
                    .replace("{{ADDRESS}}", escapeHtml(address))
                    .replace("{{AREA}}", escapeHtml(area))
                    .replace("{{CITY}}", escapeHtml(city))
                    .replace("{{SIZE}}", escapeHtml(size))
                    .replace("{{PROPERTY_TYPE}}", escapeHtml(propertyType))
                    .replace("{{PRICE}}", escapeHtml(price))
                    .replace(
                            "{{ACTION_TEXT}}",
                            updated ? "updated successfully" : "removed"
                    )
                    .replace("{{STATUS_TEXT}}", escapeHtml(statusText))
                    .replace("{{STATUS_BACKGROUND}}", statusBackground)
                    .replace("{{STATUS_BORDER}}", statusBorder)
                    .replace("{{STATUS_COLOR}}", statusColor);

        return sendEmail(to, subject, html);
    }

    public static boolean sendDealConfirmationEmail(String buyerEmail, String buyerName, String sellerName, String propertyAddress, String sellerMobile, String dealPrice, String advanceAmount, String balanceAmount, String commission, String dealDate, String registryDate, String dealStatus) {

        try {

            String subject =
                    "🏠 PropEase - Your Property Purchase is Confirmed";

            String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">

                    <style>

                        body {
                            margin: 0;
                            padding: 0;
                            background: #f4f7fb;
                            font-family: Arial, Helvetica, sans-serif;
                        }

                        .container {
                            width: 650px;
                            margin: 30px auto;
                            background: #ffffff;
                            border-radius: 20px;
                            overflow: hidden;
                            box-shadow: 0 8px 30px rgba(0,0,0,0.12);
                        }

                        .header {
                            background: linear-gradient(
                                135deg,
                                #0d47a1,
                                #1976d2,
                                #42a5f5
                            );
                            padding: 40px 30px;
                            text-align: center;
                            color: white;
                        }

                        .logo {
                            font-size: 30px;
                            font-weight: bold;
                            margin-bottom: 10px;
                        }

                        .header-title {
                            font-size: 25px;
                            font-weight: bold;
                            margin: 10px 0;
                        }

                        .header-subtitle {
                            font-size: 14px;
                            opacity: 0.9;
                        }

                        .content {
                            padding: 35px;
                            color: #263238;
                        }

                        .success-box {
                            background: #e8f5e9;
                            border-left: 5px solid #43a047;
                            padding: 18px;
                            border-radius: 10px;
                            margin-bottom: 25px;
                            color: #2e7d32;
                            font-weight: bold;
                        }

                        .greeting {
                            font-size: 17px;
                            line-height: 1.6;
                        }

                        .card {
                            background: #f8fbff;
                            border: 1px solid #dce8f5;
                            border-radius: 14px;
                            padding: 20px;
                            margin-top: 22px;
                        }

                        .card-title {
                            color: #0d47a1;
                            font-size: 19px;
                            font-weight: bold;
                            margin-bottom: 15px;
                        }

                        table {
                            width: 100%;
                            border-collapse: collapse;
                        }

                        td {
                            padding: 11px 5px;
                            border-bottom: 1px solid #e8eef5;
                            font-size: 14px;
                        }

                        .label {
                            color: #607d8b;
                            font-weight: bold;
                        }

                        .value {
                            color: #263238;
                            text-align: right;
                        }

                        .price {
                            color: #1565c0;
                            font-size: 20px;
                            font-weight: bold;
                        }

                        .status {
                            display: inline-block;
                            background: #e8f5e9;
                            color: #2e7d32;
                            padding: 6px 12px;
                            border-radius: 20px;
                            font-weight: bold;
                        }

                        .message {
                            margin-top: 25px;
                            line-height: 1.7;
                            color: #455a64;
                        }

                        .footer {
                            background: #263238;
                            color: #cfd8dc;
                            text-align: center;
                            padding: 25px;
                            font-size: 12px;
                            line-height: 1.7;
                        }

                        .footer strong {
                            color: white;
                        }

                    </style>
                </head>

                <body>

                    <div class="container">

                        <div class="header">

                            <div class="logo">
                                🏠 PropEase
                            </div>

                            <div class="header-title">
                                Your Property Purchase is Confirmed
                            </div>

                            <div class="header-subtitle">
                                Official Deal Confirmation
                            </div>

                        </div>


                        <div class="content">

                            <div class="success-box">
                                ✓ Your property purchase has been successfully finalized.
                            </div>


                            <p class="greeting">
                                Congratulations
                                <strong>{{BUYER_NAME}}</strong>! 🎉
                            </p>

                            <p class="greeting">
                                Your purchase of the property at
                                <strong>{{PROPERTY_ADDRESS}}</strong>
                                has been successfully finalized through
                                <strong>PropEase</strong>.
                            </p>


                            <div class="card">

                                <div class="card-title">
                                    🏡 Property Details
                                </div>

                                <table>

                                    <tr>
                                        <td class="label">
                                            Property Address
                                        </td>

                                        <td class="value">
                                            {{PROPERTY_ADDRESS}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Seller
                                        </td>

                                        <td class="value">
                                            {{SELLER_NAME}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Seller Mobile
                                        </td>

                                        <td class="value">
                                            {{SELLER_MOBILE}}
                                        </td>
                                    </tr>

                                </table>

                            </div>


                            <div class="card">

                                <div class="card-title">
                                    💰 Deal Summary
                                </div>

                                <table>

                                    <tr>
                                        <td class="label">
                                            Final Deal Price
                                        </td>

                                        <td class="value price">
                                            ₹{{DEAL_PRICE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Advance Paid
                                        </td>

                                        <td class="value">
                                            ₹{{ADVANCE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Balance Amount
                                        </td>

                                        <td class="value">
                                            ₹{{BALANCE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Commission
                                        </td>

                                        <td class="value">
                                            ₹{{COMMISSION}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Deal Date
                                        </td>

                                        <td class="value">
                                            {{DEAL_DATE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Registry Date
                                        </td>

                                        <td class="value">
                                            {{REGISTRY_DATE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Deal Status
                                        </td>

                                        <td class="value">
                                            <span class="status">
                                                {{DEAL_STATUS}}
                                            </span>
                                        </td>
                                    </tr>

                                </table>

                            </div>


                            <p class="message">
                                Please keep this email safely for your records.
                                It serves as confirmation that your property
                                purchase has been successfully processed.
                            </p>

                            <p class="message">
                                Thank you for choosing
                                <strong>PropEase</strong> for your real estate
                                transaction.
                            </p>

                            <p>
                                Regards,<br>
                                <strong>PropEase Team</strong>
                            </p>

                        </div>


                        <div class="footer">

                            <strong>PropEase</strong><br>

                            Real Estate Management System<br>

                            This is an automated confirmation email.
                            Please do not reply directly to this email.

                        </div>

                    </div>

                </body>
                </html>
                """

                    .replace("{{BUYER_NAME}}", escapeHtml(buyerName))
                    .replace("{{PROPERTY_ADDRESS}}", escapeHtml(propertyAddress))
                    .replace("{{SELLER_NAME}}", escapeHtml(sellerName))
                    .replace("{{SELLER_MOBILE}}", escapeHtml(sellerMobile))
                    .replace("{{DEAL_PRICE}}", escapeHtml(dealPrice))
                    .replace("{{ADVANCE}}", escapeHtml(advanceAmount))
                    .replace("{{BALANCE}}", escapeHtml(balanceAmount))
                    .replace("{{COMMISSION}}", escapeHtml(commission))
                    .replace("{{DEAL_DATE}}", escapeHtml(dealDate))
                    .replace("{{REGISTRY_DATE}}", escapeHtml(registryDate))
                    .replace("{{DEAL_STATUS}}", escapeHtml(dealStatus));

            return sendEmail(
                    buyerEmail,
                    subject,
                    html
            );

        }
        catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public static boolean sendSellerDealConfirmationEmail(String sellerEmail, String sellerName, String buyerName, String propertyAddress, String buyerMobile, String dealPrice, String advanceAmount, String balanceAmount, String commission, String dealDate, String registryDate, String dealStatus) {

        try {

            String subject =
                    "🎉 PropEase - Your Property Has Been Sold";

            String html = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">

                    <style>

                        body {
                            margin: 0;
                            padding: 0;
                            background: #f4f7fb;
                            font-family: Arial, Helvetica, sans-serif;
                        }

                        .container {
                            width: 650px;
                            margin: 30px auto;
                            background: #ffffff;
                            border-radius: 20px;
                            overflow: hidden;
                            box-shadow: 0 8px 30px rgba(0,0,0,0.12);
                        }

                        .header {
                            background: linear-gradient(
                                135deg,
                                #2e7d32,
                                #43a047,
                                #66bb6a
                            );
                            padding: 40px 30px;
                            text-align: center;
                            color: white;
                        }

                        .logo {
                            font-size: 30px;
                            font-weight: bold;
                            margin-bottom: 10px;
                        }

                        .header-title {
                            font-size: 25px;
                            font-weight: bold;
                            margin: 10px 0;
                        }

                        .header-subtitle {
                            font-size: 14px;
                            opacity: 0.9;
                        }

                        .content {
                            padding: 35px;
                            color: #263238;
                        }

                        .success-box {
                            background: #e8f5e9;
                            border-left: 5px solid #43a047;
                            padding: 18px;
                            border-radius: 10px;
                            margin-bottom: 25px;
                            color: #2e7d32;
                            font-weight: bold;
                        }

                        .greeting {
                            font-size: 17px;
                            line-height: 1.6;
                        }

                        .card {
                            background: #f8fbf8;
                            border: 1px solid #d9eadb;
                            border-radius: 14px;
                            padding: 20px;
                            margin-top: 22px;
                        }

                        .card-title {
                            color: #2e7d32;
                            font-size: 19px;
                            font-weight: bold;
                            margin-bottom: 15px;
                        }

                        table {
                            width: 100%;
                            border-collapse: collapse;
                        }

                        td {
                            padding: 11px 5px;
                            border-bottom: 1px solid #e5eee6;
                            font-size: 14px;
                        }

                        .label {
                            color: #607d8b;
                            font-weight: bold;
                        }

                        .value {
                            color: #263238;
                            text-align: right;
                        }

                        .price {
                            color: #2e7d32;
                            font-size: 20px;
                            font-weight: bold;
                        }

                        .status {
                            display: inline-block;
                            background: #e8f5e9;
                            color: #2e7d32;
                            padding: 6px 12px;
                            border-radius: 20px;
                            font-weight: bold;
                        }

                        .message {
                            margin-top: 25px;
                            line-height: 1.7;
                            color: #455a64;
                        }

                        .footer {
                            background: #263238;
                            color: #cfd8dc;
                            text-align: center;
                            padding: 25px;
                            font-size: 12px;
                            line-height: 1.7;
                        }

                        .footer strong {
                            color: white;
                        }

                    </style>
                </head>

                <body>

                    <div class="container">

                        <div class="header">

                            <div class="logo">
                                🏠 PropEase
                            </div>

                            <div class="header-title">
                                Your Property Has Been Sold
                            </div>

                            <div class="header-subtitle">
                                Official Deal Confirmation
                            </div>

                        </div>


                        <div class="content">

                            <div class="success-box">
                                ✓ Property sale successfully completed.
                            </div>


                            <p class="greeting">
                                Dear
                                <strong>{{SELLER_NAME}}</strong>,
                            </p>

                            <p class="greeting">
                                🎉 Congratulations! Your property at
                                <strong>{{PROPERTY_ADDRESS}}</strong>
                                has been successfully sold to
                                <strong>{{BUYER_NAME}}</strong>.
                            </p>


                            <div class="card">

                                <div class="card-title">
                                    🏡 Property Details
                                </div>

                                <table>

                                    <tr>
                                        <td class="label">
                                            Property Address
                                        </td>

                                        <td class="value">
                                            {{PROPERTY_ADDRESS}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Buyer
                                        </td>

                                        <td class="value">
                                            {{BUYER_NAME}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Buyer Mobile
                                        </td>

                                        <td class="value">
                                            {{BUYER_MOBILE}}
                                        </td>
                                    </tr>

                                </table>

                            </div>


                            <div class="card">

                                <div class="card-title">
                                    💰 Sale Summary
                                </div>

                                <table>

                                    <tr>
                                        <td class="label">
                                            Final Deal Price
                                        </td>

                                        <td class="value price">
                                            ₹{{DEAL_PRICE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Advance Paid
                                        </td>

                                        <td class="value">
                                            ₹{{ADVANCE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Balance Amount
                                        </td>

                                        <td class="value">
                                            ₹{{BALANCE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Commission
                                        </td>

                                        <td class="value">
                                            ₹{{COMMISSION}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Deal Date
                                        </td>

                                        <td class="value">
                                            {{DEAL_DATE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Registry Date
                                        </td>

                                        <td class="value">
                                            {{REGISTRY_DATE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td class="label">
                                            Deal Status
                                        </td>

                                        <td class="value">
                                            <span class="status">
                                                {{DEAL_STATUS}}
                                            </span>
                                        </td>
                                    </tr>

                                </table>

                            </div>


                            <p class="message">
                                Please keep this email safely for your records.
                                It serves as confirmation that the sale of your
                                property has been successfully processed.
                            </p>

                            <p class="message">
                                Thank you for choosing
                                <strong>PropEase</strong> to manage your
                                real estate transaction.
                            </p>

                            <p>
                                Regards,<br>
                                <strong>PropEase Team</strong>
                            </p>

                        </div>


                        <div class="footer">

                            <strong>PropEase</strong><br>

                            Real Estate Management System<br>

                            This is an automated confirmation email.
                            Please do not reply directly to this email.

                        </div>

                    </div>

                </body>
                </html>
                """

                    .replace("{{SELLER_NAME}}", escapeHtml(sellerName))
                    .replace("{{BUYER_NAME}}", escapeHtml(buyerName))
                    .replace("{{PROPERTY_ADDRESS}}", escapeHtml(propertyAddress))
                    .replace("{{BUYER_MOBILE}}", escapeHtml(buyerMobile))
                    .replace("{{DEAL_PRICE}}", escapeHtml(dealPrice))
                    .replace("{{ADVANCE}}", escapeHtml(advanceAmount))
                    .replace("{{BALANCE}}", escapeHtml(balanceAmount))
                    .replace("{{COMMISSION}}", escapeHtml(commission))
                    .replace("{{DEAL_DATE}}", escapeHtml(dealDate))
                    .replace("{{REGISTRY_DATE}}", escapeHtml(registryDate))
                    .replace("{{DEAL_STATUS}}", escapeHtml(dealStatus));

            return sendEmail(
                    sellerEmail,
                    subject,
                    html
            );

        }
        catch (Exception e) {

            e.printStackTrace();

            return false;
        }
    }

    public static boolean sendDealStatusEmail(String to, String recipientName, String otherPersonName, String propertyAddress, String dealPrice, String dealDate, String registryDate, String dealStatus, boolean buyer) {

        boolean cancelled = dealStatus.equalsIgnoreCase("Cancelled");

        String title;

        if (buyer) {
            title = cancelled
                    ? "Your Property Deal Has Been Cancelled ⚠️"
                    : "Your Property Deal Has Been Updated ✨";
        }
        else {
            title = cancelled
                    ? "Property Deal Has Been Cancelled ⚠️"
                    : "Your Property Deal Has Been Updated ✨";
        }

        String subject =
                cancelled
                        ? "PropEase - Deal Cancelled"
                        : "PropEase - Deal Status Updated";

        String message;

        if (buyer) {

            message = cancelled
                    ? "The property deal for <strong>{{PROPERTY}}</strong> has been cancelled."
                    : "The property deal for <strong>{{PROPERTY}}</strong> has been updated successfully.";

        }
        else {

            message = cancelled
                    ? "The property deal for <strong>{{PROPERTY}}</strong> with <strong>{{OTHER_PERSON}}</strong> has been cancelled."
                    : "The property deal for <strong>{{PROPERTY}}</strong> with <strong>{{OTHER_PERSON}}</strong> has been updated successfully.";
        }


        String html = """
        <!DOCTYPE html>
        <html>

        <head>
            <meta charset="UTF-8">
        </head>

        <body style="
            margin:0;
            padding:0;
            background:#f1f5f9;
            font-family:Arial,Helvetica,sans-serif;
        ">

        <table width="100%" cellpadding="0" cellspacing="0"
               style="background:#f1f5f9;padding:30px 10px;">

            <tr>
                <td align="center">

                    <table width="620"
                           cellpadding="0"
                           cellspacing="0"
                           style="
                               max-width:620px;
                               width:100%;
                               background:#ffffff;
                               border-radius:20px;
                               overflow:hidden;
                               box-shadow:0 8px 30px rgba(0,0,0,0.08);
                           ">

                        <!-- HEADER -->

                        <tr>
                            <td style="
                                background:linear-gradient(
                                    135deg,
                                    #0d47a1,
                                    #1976d2,
                                    #42a5f5
                                );
                                padding:35px;
                                text-align:center;
                            ">

                                <div style="font-size:44px;">
                                    🤝
                                </div>

                                <h1 style="
                                    margin:8px 0;
                                    color:white;
                                    font-size:30px;
                                ">
                                    PropEase
                                </h1>

                                <p style="
                                    margin:0;
                                    color:#dbeafe;
                                    font-size:14px;
                                ">
                                    Real Estate Management System
                                </p>

                            </td>
                        </tr>


                        <!-- CONTENT -->

                        <tr>
                            <td style="padding:35px 40px;">

                                <h2 style="
                                    margin:0 0 12px;
                                    color:#172554;
                                    font-size:24px;
                                ">
                                    {{TITLE}}
                                </h2>

                                <p style="
                                    color:#64748b;
                                    font-size:15px;
                                    line-height:1.7;
                                ">
                                    Dear <strong>{{NAME}}</strong>,
                                </p>

                                <p style="
                                    color:#64748b;
                                    font-size:14px;
                                    line-height:1.7;
                                ">
                                    {{MESSAGE}}
                                </p>


                                <div style="
                                    margin:25px 0;
                                    padding:16px;
                                    background:{{STATUS_BACKGROUND}};
                                    border:1px solid {{STATUS_BORDER}};
                                    border-radius:12px;
                                    color:{{STATUS_COLOR}};
                                    font-weight:bold;
                                ">
                                    {{STATUS}}
                                </div>


                                <!-- DEAL DETAILS -->

                                <table width="100%"
                                       cellpadding="0"
                                       cellspacing="0"
                                       style="
                                           border:1px solid #e2e8f0;
                                           border-radius:14px;
                                           overflow:hidden;
                                       ">

                                    <tr>
                                        <td colspan="2"
                                            style="
                                                background:#f8fafc;
                                                padding:16px;
                                                font-weight:bold;
                                                color:#0f172a;
                                            ">
                                            🤝 Deal Details
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="
                                            padding:12px 18px;
                                            color:#64748b;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            Property
                                        </td>

                                        <td style="
                                            padding:12px 18px;
                                            text-align:right;
                                            font-weight:bold;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            {{PROPERTY}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="
                                            padding:12px 18px;
                                            color:#64748b;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            {{OTHER_LABEL}}
                                        </td>

                                        <td style="
                                            padding:12px 18px;
                                            text-align:right;
                                            font-weight:bold;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            {{OTHER_PERSON}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="
                                            padding:12px 18px;
                                            color:#64748b;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            Deal Price
                                        </td>

                                        <td style="
                                            padding:12px 18px;
                                            text-align:right;
                                            color:#0d47a1;
                                            font-weight:bold;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            ₹{{PRICE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="
                                            padding:12px 18px;
                                            color:#64748b;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            Deal Date
                                        </td>

                                        <td style="
                                            padding:12px 18px;
                                            text-align:right;
                                            font-weight:bold;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            {{DEAL_DATE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="
                                            padding:12px 18px;
                                            color:#64748b;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            Registry Date
                                        </td>

                                        <td style="
                                            padding:12px 18px;
                                            text-align:right;
                                            font-weight:bold;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            {{REGISTRY_DATE}}
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="
                                            padding:12px 18px;
                                            color:#64748b;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            Current Status
                                        </td>

                                        <td style="
                                            padding:12px 18px;
                                            text-align:right;
                                            font-weight:bold;
                                            color:#1976d2;
                                            border-top:1px solid #e2e8f0;
                                        ">
                                            {{DEAL_STATUS}}
                                        </td>
                                    </tr>

                                </table>


                                <p style="
                                    margin-top:25px;
                                    color:#64748b;
                                    font-size:14px;
                                    line-height:1.7;
                                ">
                                    Please keep this email for your records.
                                </p>

                                <p style="
                                    color:#334155;
                                    font-size:14px;
                                ">
                                    Regards,<br>
                                    <strong>PropEase Team</strong>
                                </p>

                            </td>
                        </tr>


                        <!-- FOOTER -->

                        <tr>
                            <td style="
                                background:#0f172a;
                                padding:25px;
                                text-align:center;
                            ">

                                <strong style="color:white;">
                                    PropEase
                                </strong>

                                <p style="
                                    margin:8px 0 0;
                                    color:#94a3b8;
                                    font-size:12px;
                                ">
                                    Smart Property Management System<br>
                                    This is an automated email.
                                </p>

                            </td>
                        </tr>

                    </table>

                </td>
            </tr>

        </table>

        </body>
        </html>
        """

                .replace("{{TITLE}}", escapeHtml(title))
                .replace("{{NAME}}", escapeHtml(recipientName))
                .replace("{{PROPERTY}}", escapeHtml(propertyAddress))
                .replace("{{OTHER_PERSON}}", escapeHtml(otherPersonName))
                .replace("{{PRICE}}", escapeHtml(dealPrice))
                .replace("{{DEAL_DATE}}", escapeHtml(dealDate))
                .replace("{{REGISTRY_DATE}}", escapeHtml(registryDate))
                .replace("{{DEAL_STATUS}}", escapeHtml(dealStatus))
                .replace("{{MESSAGE}}", message)
                .replace(
                        "{{OTHER_LABEL}}",
                        buyer ? "Seller" : "Buyer"
                )
                .replace(
                        "{{STATUS}}",
                        cancelled
                                ? "⚠ Deal Cancelled"
                                : "✓ Deal Details Updated"
                )
                .replace(
                        "{{STATUS_BACKGROUND}}",
                        cancelled ? "#fef2f2" : "#ecfdf5"
                )
                .replace(
                        "{{STATUS_BORDER}}",
                        cancelled ? "#fecaca" : "#a7f3d0"
                )
                .replace(
                        "{{STATUS_COLOR}}",
                        cancelled ? "#b91c1c" : "#047857"
                );

        return sendEmail(to, subject, html);
    }


    private static String escapeHtml(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

}