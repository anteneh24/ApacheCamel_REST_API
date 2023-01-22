package com.example.first;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.context.annotation.Bean;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;
//import javax.xml.soap.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Base64;

//import jdk.internal.org.jline.utils.Log;
//import org.apache.camel.main.Main;


public class Sample {
    private static SSLSocketFactory sslSocketFactory = null;
    private static SecretKeySpec secretKey;
    private static byte[] key;
    private static final String ALGORITHM = "AES";


//    public static void main(String[] args){
////        String soapEndpointUrl = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso?WSDL";
////        String soapAction = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso";
////        String url = "http://webservices.oorsprong.org/websamples.countryinfo/CountryInfoService.wso";
//////        request(url);
//////        callSoapWebService(soapEndpointUrl, soapAction);
////        Double balance = 0.0;
//////        String balanceString = requestTransfer("1000101160001","1000200140001","300");
//        String balanceString = requestAccountInfo("00251920966435");
//
////        try {
////            request(new URL("https://172.26.5.7:8543/imal_core_cpws_imal_trn/pathservices/processStatementOfAccount"),false);
////            request(new URL("https://172.26.5.7:8543/imal_core_cpws_imal_trn/pathservices/processStatementOfAccount"),true);
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
//
////        System.out.println(balanceString);
//
//
//
//        String phone ="TERRAPY";
////        phone = cleanPhone(phone);
//
//        double money = 10800.1;
//        NumberFormat formatter = NumberFormat.getCurrencyInstance();
//        String moneyString = formatter.format(money);
//        moneyString = moneyString.substring(1,moneyString.length())+" Birr";
//        System.out.println(phone);
//        String encrypt = encrypt(phone,"String");
//        System.out.println(encrypt);
//        System.out.println(decrypt(encrypt,"String"));
////        phone = phone.substring(1,phone.length())+" Birr";
////        System.out.println(phone);
//
//    }


    public static void prepareSecreteKey(String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes(StandardCharsets.UTF_8);
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(String strToEncrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(String strToDecrypt, String secret) {
        try {
            prepareSecreteKey(secret);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }

    private static String cleanPhone(String phone) {
        if(phone.startsWith("9")) phone = "0".concat(phone);
        String clean = phone;
        if(phone.startsWith("+251")){
            clean = phone.replace("+251", "00251");
        }
        if(phone.startsWith("251")){
            clean = phone.replace("251", "00251");
        }
        if(phone.startsWith("09")){
            clean = phone.replaceAll("^09", "00251");
        }
        clean = clean.replace("(", "");
        clean = clean.replace(")", "");
        return clean;
    }

    public static boolean isNumeric(String string) {
        double intValue;


        if(string == null || string.equals("")) {
            System.out.println("String cannot be parsed, it is null or empty.");
            return false;
        }

        try {
            intValue = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
            System.out.println("Input String cannot be parsed to Integer.");
        }
        return false;
    }


    private static void request(String url) {
        try {
            url = "http://www.holidaywebservice.com/HolidayService_v2/HolidayService2.asmx?op=GetHolidaysAvailable";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
            String xml ="<?xml version=\"1.0\"?>\n" +
                    "<x:Envelope xmlns:x=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                    "xmlns:web=\"http://www.oorsprong.org/websamples.countryinfo\">" +
                    "<x:Header/><x:Body><web:ListOfContinentsByCode/>" +
                    "</x:Body></x:Envelope>";
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(xml);
            wr.flush();
            wr.close();
            String responseStatus = con.getResponseMessage();
            System.out.println(responseStatus);
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject jsonObject = XML.toJSONObject(response.toString());
            System.out.println(jsonObject.toString());
            JSONArray jsonArray = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").
                    getJSONObject("m:ListOfContinentsByCodeResponse").
                    getJSONObject("m:ListOfContinentsByCodeResult").getJSONArray("m:tContinent");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject object = (JSONObject) jsonArray.get(i);
                System.out.println("response: code:"+object.get("m:sCode").toString()+" name:"+object.get("m:sName").toString());
            }


        } catch (Exception e) {
            System.out.println(e);
        }


    }

    private static String requestBalance(String accountNumber) {
        try {
            String url = "https://172.26.5.7:8543/imal_core_cpws_imal_trn/pathservices/processStatementOfAccount";
            URL obj = new URL(url);

            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
           con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
            String xml ="<?xml version=\"1.0\"?>\n" +
                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:stat=\"statementOfAccountWs\">" +
                    "<soapenv:Header/>" +
                    "<soapenv:Body>" +
                    "<stat:returnAvailableBalance><serviceContext><businessArea>Retail</businessArea><businessDomain>Products</businessDomain><operationName>returnAvailableBalance</operationName><serviceDomain>StatementOfAccount</serviceDomain><serviceID>4701</serviceID><version>1.0</version></serviceContext><companyCode>1</companyCode>" +
                    "<account>" +
                    "<additionalRef>"+accountNumber+"</additionalRef>" +
                    "</account>" +
                    "<requesterContext><langId>EN</langId><password>VGVzdDQ1Ng==</password><requesterTimeStamp>2022-06-12T09:00:00</requesterTimeStamp><userID>TERRAPY</userID></requesterContext><vendorContext><license>Copyright 2018 Path Solutions. All Rights Reserved</license><providerCompanyName>Path Solutions</providerCompanyName><providerID>IMAL</providerID></vendorContext></stat:returnAvailableBalance></soapenv:Body></soapenv:Envelope>\n";
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(xml);
            wr.flush();
            wr.close();
            String responseStatus = con.getResponseMessage();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            JSONObject jsonObject = XML.toJSONObject(response.toString());
            System.out.println(response.toString());
            Object cvAvailableBalance = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("ns2:returnAvailableBalanceResponse").get("cvAvailableBalance");

            return cvAvailableBalance.toString();

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }

    }

    public static void request(URL url, boolean enableCertCheck) throws Exception {
        BufferedReader reader = null;
        // Repeat several times to check persistence.
        System.out.println("Cert checking=["+(enableCertCheck?"enabled":"disabled")+"]");
        for (int i = 0; i < 5; ++i) {
            try {

                HttpURLConnection httpConnection = (HttpsURLConnection) url.openConnection();

                // Normally, instanceof would also be used to check the type.
                if( ! enableCertCheck ) {
                    setAcceptAllVerifier((HttpsURLConnection)httpConnection);
                }

                reader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()), 1);

                char[] buf = new char[1024];
                StringBuilder sb = new StringBuilder();
                int count = 0;
                while( -1 < (count = reader.read(buf)) ) {
                    sb.append(buf, 0, count);
                }
                System.out.println(sb.toString());

                reader.close();

            } catch (IOException ex) {
                System.out.println(ex);

                if( null != reader ) {
                    reader.close();
                }
            }
        }
    }
    protected static void setAcceptAllVerifier(HttpsURLConnection connection) throws NoSuchAlgorithmException, KeyManagementException {

        // Create the socket factory.
        // Reusing the same socket factory allows sockets to be
        // reused, supporting persistent connections.
        if( null == sslSocketFactory) {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, ALL_TRUSTING_TRUST_MANAGER, new java.security.SecureRandom());
            sslSocketFactory = sc.getSocketFactory();
        }

        connection.setSSLSocketFactory(sslSocketFactory);
        String xml ="<?xml version=\"1.0\"?>\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:stat=\"statementOfAccountWs\">" +
                "<soapenv:Header/>" +
                "<soapenv:Body>" +
                "<stat:returnAvailableBalance><serviceContext><businessArea>Retail</businessArea><businessDomain>Products</businessDomain><operationName>returnAvailableBalance</operationName><serviceDomain>StatementOfAccount</serviceDomain><serviceID>4701</serviceID><version>1.0</version></serviceContext><companyCode>1</companyCode>" +
                "<account>" +
                "<additionalRef>1000200140001</additionalRef>" +
                "</account>" +
                "<requesterContext><langId>EN</langId><password>VGVzdDQ1Ng==</password><requesterTimeStamp>2022-06-12T09:00:00</requesterTimeStamp><userID>TERRAPY</userID></requesterContext><vendorContext><license>Copyright 2018 Path Solutions. All Rights Reserved</license><providerCompanyName>Path Solutions</providerCompanyName><providerID>IMAL</providerID></vendorContext></stat:returnAvailableBalance></soapenv:Body></soapenv:Envelope>\n";
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
        DataOutputStream wr = null;
        try {
            wr = new DataOutputStream(connection.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            wr.writeBytes(xml);
            wr.flush();
            wr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Since we may be using a cert with a different name, we need to ignore
        // the hostname as well.
        connection.setHostnameVerifier(ALL_TRUSTING_HOSTNAME_VERIFIER);
    }

    private static final TrustManager[] ALL_TRUSTING_TRUST_MANAGER = new TrustManager[] {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
    };

    private static final HostnameVerifier ALL_TRUSTING_HOSTNAME_VERIFIER  = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
//
//    public static void trustAllHosts()
//    {
//        disableSslVerification();
//        try
//        {
//            TrustManager[] trustAllCerts = new TrustManager[]{
//                    new X509ExtendedTrustManager()
//                    {
//                        @Override
//                        public java.security.cert.X509Certificate[] getAcceptedIssuers()
//                        {
//                            return null;
//                        }
//
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
//                        {
//                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
//                        {
//                        }
//
////                        @Override
////                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
////                        {
////
////                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, Socket socket) throws CertificateException
//                        {
//
//                        }
//
//                        @Override
//                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {
//
//                        }
//
//                        @Override
//                        public void checkClientTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException
//                        {
//
//                        }
//
//                        @Override
//                        public void checkServerTrusted(java.security.cert.X509Certificate[] xcs, String string, SSLEngine ssle) throws CertificateException
//                        {
//
//                        }
//
//                    }
//            };
//
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//
//            // Create all-trusting host name verifier
//            HostnameVerifier allHostsValid = new  HostnameVerifier()
//            {
//                @Override
//                public boolean verify(String hostname, SSLSession session)
//                {
//                    return true;
//                }
//            };
//            // Install the all-trusting host verifier
//            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//        }
//        catch (Exception e)
//        {
//            System.out.println("Error occurred "+e);
//        }
//    }
//
//    private static void disableSslVerification() {
//        try
//        {
//            // Create a trust manager that does not validate certificate chains
//            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
//                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
//                    return null;
//                }
//                public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                }
//                public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                }
//            }
//            };
//
//            // Install the all-trusting trust manager
//            SSLContext sc = SSLContext.getInstance("SSL");
//            sc.init(null, trustAllCerts, new java.security.SecureRandom());
//            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//
//            // Create all-trusting host name verifier
//            HostnameVerifier allHostsValid = new HostnameVerifier() {
//                public boolean verify(String hostname, SSLSession session) {
//                    return true;
//                }
//            };
//
//            // Install the all-trusting host verifier
//            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        } catch (KeyManagementException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static SOAPMessage sendSoapRequest(String endpointUrl, SOAPMessage request) {
//        try {
//            final boolean isHttps = endpointUrl.toLowerCase().startsWith("https");
//            HttpsURLConnection httpsConnection = null;
//            // Open HTTPS connection
//            if (isHttps) {
//                // Create SSL context and trust all certificates
//                SSLContext sslContext = SSLContext.getInstance("SSL");
//                TrustManager[] trustAll
//                        = new TrustManager[] {new TrustAllCertificates()};
//                sslContext.init(null, trustAll, new java.security.SecureRandom());
//                // Set trust all certificates context to HttpsURLConnection
//                HttpsURLConnection
//                        .setDefaultSSLSocketFactory(sslContext.getSocketFactory());
//                // Open HTTPS connection
//                URL url = new URL(endpointUrl);
//                httpsConnection = (HttpsURLConnection) url.openConnection();
//                // Trust all hosts
//                httpsConnection.setHostnameVerifier(new TrustAllHosts());
//                // Connect
//                httpsConnection.connect();
//            }
//            // Send HTTP SOAP request and get response
//            SOAPConnection soapConnection
//                    = SOAPConnectionFactory.newInstance().createConnection();
//            SOAPMessage response = soapConnection.call(request, endpointUrl);
//            // Close connection
//            soapConnection.close();
//            // Close HTTPS connection
//            if (isHttps) {
//                httpsConnection.disconnect();
//            }
//            return response;
//        } catch (SOAPException | IOException
//                | NoSuchAlgorithmException | KeyManagementException ex) {
//            // Do Something
//        }
//        return null;
//    }
//
//    private static class TrustAllCertificates implements X509TrustManager {
//        public void checkClientTrusted(X509Certificate[] certs, String authType) {
//        }
//
//        public void checkServerTrusted(X509Certificate[] certs, String authType) {
//        }
//
//        public X509Certificate[] getAcceptedIssuers() {
//            return null;
//        }
//    }
//
//    private static class TrustAllHosts implements HostnameVerifier {
//        public boolean verify(String hostname, SSLSession session) {
//            return true;
//        }
//    }
//
//    private static String requestTransfer(String fromAccount,String toAccount,String amount) {
//        try {
//            String url = "http://172.26.5.7:8081/imal_core_cpws_imal_trn/pathservices/processTransfer?wsdl";
//            URL obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
//
//            String xml ="<?xml version=\"1.0\"?>\n" +
//                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:tran=\"transferWs\">" +
//                    "<soapenv:Header/>" +
//                    "<soapenv:Body>" +
//                    "<tran:createTransfer><serviceContext><businessArea>Retail</businessArea><businessDomain>PaymentsOperationsManagement</businessDomain><operationName>createTransfer</operationName><serviceDomain>Transfer</serviceDomain><serviceID>4801</serviceID><version>1.0</version></serviceContext><companyCode>1</companyCode><branchCode>2</branchCode><transactionType>3</transactionType>" +
//                    "<fromAccount><additionalRef>"+fromAccount+"</additionalRef></fromAccount>" +
//                    "<toAccounts>" +
//                    "<multiAccount>" +
//                    "<account><additionalRef>"+toAccount+"</additionalRef></account>" +
//                    "<amount>500000</amount>" +
//                    "</multiAccount>" +
//                    "</toAccounts>" +
//                    "<transactionAmount>"+amount+"</transactionAmount>" +
//                    "<currencyIso>230</currencyIso><transactionDate>"+java.time.LocalDate.now().toString()+"</transactionDate><useDate>1</useDate><requesterContext><langId>EN</langId><password>VGVzdDQ1Ng==</password><requesterTimeStamp>2022-06-12T09:00:00</requesterTimeStamp><userID>TERRAPY</userID></requesterContext></tran:createTransfer></soapenv:Body></soapenv:Envelope>\n";
//            System.out.println(""+java.time.LocalDate.now().toString());
//            con.setDoOutput(true);
//            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//            wr.writeBytes(xml);
//            wr.flush();
//            wr.close();
//            String responseStatus = con.getResponseMessage();
//            BufferedReader in = new BufferedReader(new InputStreamReader(
//                    con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            JSONObject jsonObject = XML.toJSONObject(response.toString());
//            Object statusDesc = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("ns2:createTransferResponse").get("transactionNumber");
//
//            return statusDesc.toString();
//
//        } catch (Exception e) {
//            System.out.println(e);
//            return null;
//        }
//
//    }
//
//    private static String requestMiniStatement(String account) {
//        try {
//            String url = "http://172.26.5.7:8081/imal_core_cpws_imal_trn/pathservices/processStatementOfAccount";
//            URL obj = new URL(url);
//            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
//            String xml ="<?xml version=\"1.0\"?>\n" +
//                    "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:stat=\"statementOfAccountWs\">" +
//                    "<soapenv:Header/><soapenv:Body>" +
//                    "<stat:returnMiniStatement><serviceContext><businessArea>Retail</businessArea><businessDomain>Products</businessDomain><operationName>returnMiniStatement</operationName><serviceDomain>StatementOfAccount</serviceDomain><serviceID>4703</serviceID><version>1.0</version></serviceContext><companyCode>1</companyCode><branchCode>2</branchCode>" +
//                    "<account>" +
//                    "<additionalRef>"+account+"</additionalRef>" +
//                    "</account>" +
//                    "<lastN>5</lastN><requesterContext><langId>EN</langId><password>VGVzdDQ1Ng==</password><requesterTimeStamp>2022-06-12T09:00:00</requesterTimeStamp><userID>TERRAPY</userID></requesterContext><vendorContext><license>Copyright 2018 Path Solutions. All Rights Reserved</license><providerCompanyName>Path Solutions</providerCompanyName><providerID>IMAL</providerID></vendorContext></stat:returnMiniStatement></soapenv:Body></soapenv:Envelope>\n";
//            con.setDoOutput(true);
//            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
//            wr.writeBytes(xml);
//            wr.flush();
//            wr.close();
//            String responseStatus = con.getResponseMessage();
//            BufferedReader in = new BufferedReader(new InputStreamReader(
//                    con.getInputStream()));
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = in.readLine()) != null) {
//                response.append(inputLine);
//            }
//            in.close();
//            JSONObject jsonObject = XML.toJSONObject(response.toString());
//            System.out.println(response.toString());
//            JSONArray miniStatements = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("ns2:returnMiniStatementResponse").getJSONObject("miniStatementList").getJSONArray("miniStatement");
//            String result = "";
//            for(int i =0;i<miniStatements.length();i++){
//                JSONObject statement = (JSONObject)miniStatements.get(i);
//                if(statement.get("transactionType").toString().equals("C"))
//                    result+= "amount:"+statement.get("amount").toString()+" Transaction Type:Deposit\n";
//                else
//                    result+= "amount:"+(-1*Double.parseDouble(statement.get("amount").toString()))+" Transaction Type:Withdraw\n";
//
//            }
//            return result;
//
//        } catch (Exception e) {
//            System.out.println(e);
//            return null;
//        }
//
//    }
//
//
public static String requestAccountInfo(String phoneNumber){
    JSONObject jsonObject = null;
    try {
        String url = "https://172.26.5.7:8543/imal_core_pws_imal_trn/pathservices/processGeneralAccounts";
        URL obj = new URL(url);
//        System.setProperty("javax.net.ssl.trustStore",
//                "src/main/resources/HijraBank.cer");
        System.setProperty("Djavax.net.ssl.keyStoreType","pkcs12");
        System.setProperty("Djavax.net.ssl.trustStoreType","jks");
        System.setProperty("Djavax.net.ssl.trustStorePassword", "123456");
//        System.setProperty("javax.net.ssl.keyStorePassword","123456");
//        System.setProperty("javax.net.ssl.keyStore","hijraBank");
        System.setProperty("Djavax.net.ssl.trustStore","gridserver.keystore");

                SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
//        InputStream stream = loader.getResourceAsStream("/myProp.properties");
        con.setSSLSocketFactory(sslsocketfactory);
        con.setRequestMethod("GET");
        con.setRequestProperty("Content-Type","application/soap+xml; charset=utf-8");
        con.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession sslSession) {
                return true;
            }
        });
        String xml ="<?xml version=\"1.0\"?>\n" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:gen=\"generalAccountsWs\">" +
                "<soapenv:Header/><soapenv:Body>" +
                "<gen:returnGeneralAccountsList><serviceContext><businessArea>Retail</businessArea><businessDomain>Products</businessDomain><operationName>returnGeneralAccountsList</operationName><serviceDomain>GeneralAccounts</serviceDomain><serviceID>803</serviceID><version>1.0</version></serviceContext><companyCode>1</companyCode><branchCode>1</branchCode><dynamicFilter><allAny>All</allAny>" +
                "<filters><filter><key>mobile</key><operator>eq</operator>" +
                "<value>"+phoneNumber+"</value>" +
                "</filter></filters>" +
                "</dynamicFilter><requesterContext><langId>EN</langId><password>VGVzdDQ1Ng==</password><requesterTimeStamp>2022-06-12T09:00:00</requesterTimeStamp><userID>TERRAPY</userID></requesterContext><vendorContext><license>Copyright 2018 Path Solutions. All Rights Reserved</license><providerCompanyName>Path Solutions</providerCompanyName><providerID>IMAL</providerID></vendorContext></gen:returnGeneralAccountsList></soapenv:Body></soapenv:Envelope>\n";
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(xml);
        wr.flush();
        wr.close();
        String responseStatus = con.getResponseMessage();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
//         jsonObject= XML.toJSONObject(response.toString());
        System.out.println(response.toString());
//        Object cvAvailable = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("ns2:returnGeneralAccountsListResponse").getJSONObject("serviceResponse").get("statusCode");
//
//        JSONObject object = new JSONObject();
//        object.put("status","notfound");
//
//        if(Integer.parseInt(cvAvailable.toString())==0) {
//            object.put("status", "found");
//            JSONObject accou =
//            object.put("accountList", jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").
//                    getJSONObject("ns2:returnGeneralAccountsListResponse").getJSONObject("accountsList").getJSONArray("accountDC").getJSONObject(0).getJSONObject("account").get("additionalRef"));
//
//        }

//        System.out.println(object.toString());

        return "";

    } catch (Exception e) {
        System.out.println(e.getMessage().toString());
//        Object cvAvailable = jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").getJSONObject("ns2:returnGeneralAccountsListResponse").getJSONObject("serviceResponse").get("statusCode");
//
//        JSONObject object = new JSONObject();
//        object.put("status","notfound");
//
//        if(Integer.parseInt(cvAvailable.toString())==0) {
//            object.put("status", "found");
//            object.put("accountList", jsonObject.getJSONObject("soap:Envelope").getJSONObject("soap:Body").
//                    getJSONObject("ns2:returnGeneralAccountsListResponse").getJSONObject("accountsList").getJSONObject("accountDC").getJSONObject("account").get("additionalRef"));
//
//        }
        return "";
    }

}
//
//    static class SimpleX509TrustManager implements X509TrustManager {
//        public void checkClientTrusted(
//                X509Certificate[] cert, String s)
//                throws CertificateException {
//        }
//
//        public void checkServerTrusted(
//                X509Certificate[] cert, String s)
//                throws CertificateException {
//        }
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            // TODO Auto-generated method stub
//            return null;
//        }
//
//    }


//    @Bean
//    public WebServiceTemplate webServiceTemplate(){
////        WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
////        MessageFactory msgFactory = null;
////        try {
////            msgFactory = MessageFactory.newInstance(SOAPConstants.DEFAULT_SOAP_PROTOCOL);
////        } catch (SOAPException e) {
////            // TODO Auto-generated catch block
////            e.printStackTrace();
////        }
////        SaajSoapMessageFactory newSoapMessageFactory = new SaajSoapMessageFactory(msgFactory);
////        webServiceTemplate.setMessageFactory(newSoapMessageFactory);
////        webServiceTemplate.setMarshaller(marshaller());
//        webServiceTemplate.setUnmarshaller(marshaller());
//        return webServiceTemplate;
//    }
//
////and then wired into the client component and set system properties for https proxy
//
//    public GetDaysUntilPasswordExpiresResponse getGetDaysUntilPasswordExpires(  ){
//        System.getProperties().put("https.proxyHost", HTTPS_PROXY_HOST );
//        System.getProperties().put("https.proxyPort", HTTPS_PROXY_PORT);
//
//        System.getProperties().put("https.proxyUser", HTTPS_PROXY_USER );
//        System.getProperties().put("https.proxyPassword", HTTPS_PROXY_PASSWORD);
//        GetDaysUntilPasswordExpires requestPayload = new GetDaysUntilPasswordExpires();
//        requestPayload.setContext( healthClientContext());
//
//        log.info("Requesting expire info for :" + healthClientContext.getUserID());
//        GetDaysUntilPasswordExpiresResponse response = (GetDaysUntilPasswordExpiresResponse) webServiceTemplate
//                .marshalSendAndReceive(
//                        VENDOR_WS_URL+"/Account"
//                        ,requestPayload
//                        ,new SoapActionCallback(VENDOR_WS_NAMESPACE_URL+"/IAccount/GetDaysUntilPasswordExpires"){
//                            @Override
//                            public void doWithMessage(WebServiceMessage message)
//                                    throws IOException {
//                                SaajSoapMessage soapMessage = (SaajSoapMessage) message;
//                                soapMessage.setSoapAction(VENDOR_WS_NAMESPACE_URL+"/IAccount/GetDaysUntilPasswordExpires");
//                            }
//                        }
//                );
//        return response;
//    }



}
