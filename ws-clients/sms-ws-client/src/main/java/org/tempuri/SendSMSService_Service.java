package org.tempuri;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.14.redhat-1
 * 2019-01-05T23:45:47.539+06:00
 * Generated source version: 2.7.14.redhat-1
 * 
 */
@WebServiceClient(name = "SendSMSService", 
                  wsdlLocation = "file:/home/baur/git/toolpar/tastamat-main/ws-clients/sms-ws-client/src/main/resources/kaz_info.wsdl",
                  targetNamespace = "http://tempuri.org/") 
public class SendSMSService_Service extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("http://tempuri.org/", "SendSMSService");
    public final static QName SendSMSServicePort = new QName("http://tempuri.org/", "SendSMSServicePort");
    static {
        URL url = null;
        try {
            url = new URL("file:/home/baur/git/toolpar/tastamat-main/ws-clients/sms-ws-client/src/main/resources/kaz_info.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(SendSMSService_Service.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/home/baur/git/toolpar/tastamat-main/ws-clients/sms-ws-client/src/main/resources/kaz_info.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public SendSMSService_Service(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public SendSMSService_Service(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public SendSMSService_Service() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public SendSMSService_Service(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public SendSMSService_Service(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public SendSMSService_Service(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns SendSMSService
     */
    @WebEndpoint(name = "SendSMSServicePort")
    public SendSMSService getSendSMSServicePort() {
        return super.getPort(SendSMSServicePort, SendSMSService.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns SendSMSService
     */
    @WebEndpoint(name = "SendSMSServicePort")
    public SendSMSService getSendSMSServicePort(WebServiceFeature... features) {
        return super.getPort(SendSMSServicePort, SendSMSService.class, features);
    }

}
