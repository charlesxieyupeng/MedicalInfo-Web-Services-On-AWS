//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.3-hudson-jaxb-ri-2.2.3-3- 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.03.10 at 02:20:14 PM EDT 
//


package edu.stevens.cs548.clinic.service.dto.provider;

import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;

public class Adapter1
    extends XmlAdapter<String, Date>
{
    public Date unmarshal(String value) {
        return (DateAdapter.parseDate(value));
    }

    public String marshal(Date value) {
        return (DateAdapter.printDate(value));
    }
}
