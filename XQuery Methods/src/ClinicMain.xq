(: XQuery main module :)
import schema namespace p =  "http://www.example.org/schemas/clinic/Patient" at "Patient.xsd";
import schema namespace c ="http://www.example.org/schemas/clinic" at "Clinic.xsd"; 
import schema namespace t ="http://www.example.org/schemas/clinic/Treatment" at "Treatment.xsd"; 
import schema namespace pr = "http://www.example.org/schemas/clinic/Provider" at "Provider.xsd";
import module namespace g = 'http://www.example.com/schemas/clinic/theGetModule' at 'theGetModule.xq';
declare namespace ps = "http://www.example.org/xquery/clinic";

let  $clinic :=doc("ClinicSample.xml")/c:Clinic
return g:getDrugInfo($clinic)
