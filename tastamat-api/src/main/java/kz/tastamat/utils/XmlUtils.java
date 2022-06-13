package kz.tastamat.utils;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.SOAPMessage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;

public class XmlUtils {

    public static void toSoapMessageString(Vertx vertx, Object req, Handler<AsyncResult<String>> handler) {
        vertx.executeBlocking(future -> {
            try {
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                Marshaller marshaller = JAXBContext.newInstance(req.getClass()).createMarshaller();
                marshaller.marshal(req, document);

                SOAPMessage soapMessage = MessageFactory.newInstance().createMessage();
                soapMessage.getSOAPBody().addDocument(document);

                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    soapMessage.writeTo(outputStream);
                    future.complete(new String(outputStream.toByteArray()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                future.fail(e);
            }
        }, false, handler);
    }

    public static <T> void fromSoapMessage(Vertx vertx, String responseXml, MultiMap headers, Class<T> clazz, Handler<AsyncResult<T>> handler) {
        vertx.executeBlocking(future -> {
            try {
                JAXBContext jc = JAXBContext.newInstance(clazz);
                Unmarshaller unmarshaller = jc.createUnmarshaller();

                MimeHeaders mimeHeaders = new MimeHeaders();
//				headers.entries().forEach(e -> mimeHeaders.addHeader(e.getKey(), e.getValue()));

                SOAPMessage soapMessage = MessageFactory.newInstance().createMessage(mimeHeaders, new ByteArrayInputStream(responseXml.getBytes("UTF-8")));

                if (soapMessage.getSOAPBody().getFault() == null) {
                    JAXBElement<T> je = unmarshaller.unmarshal(soapMessage.getSOAPBody().getFirstChild(), clazz);
                    future.complete(je.getValue());
                } else {
                    StringBuilder message = new StringBuilder(soapMessage.getSOAPBody().getFault().getFaultString());
                    future.fail(message.toString());
                }
            } catch (Exception e) {
                future.fail(e);
            }
        }, false, handler);
    }

    public static Document convert(String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new InputSource(new StringReader(xml)));
            return doc;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void toXml(Vertx vertx, Object req, Handler<AsyncResult<String>> handler) {
        vertx.executeBlocking(future -> {
            try {
                Marshaller marshaller = JAXBContext.newInstance(req.getClass()).createMarshaller();
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    marshaller.marshal(req, outputStream);
                    future.complete(new String(outputStream.toByteArray()));
                }
            } catch (Exception e) {
                e.printStackTrace();
                future.fail(e);
            }
        }, false, handler);
    }
}
