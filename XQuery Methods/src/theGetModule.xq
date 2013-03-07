module namespace g = 'http://www.example.com/schemas/clinic/theGetModule';

import schema namespace p =  "http://www.example.org/schemas/clinic/Patient" at "Patient.xsd";
import schema namespace c ="http://www.example.org/schemas/clinic" at "Clinic.xsd"; 
import schema namespace t ="http://www.example.org/schemas/clinic/Treatment" at "Treatment.xsd"; 
import schema namespace pr = "http://www.example.org/schemas/clinic/Provider" at "Provider.xsd";

declare function g:getPatientTreatments ($id as xs:string?,$k as element(c:Clinic)?)
    as element(PatientTreatments) {<PatientTreatments>{
           for $patient in $k/p:Patient
           return if($patient/p:patient-id eq $id)
                    then $patient/p:treatments/p:treatment
                  else ()
   }</PatientTreatments> };
    
declare function g:getPatientDrugs ($id as xs:string?,$k as element(c:Clinic)?)
    as element(Patient-Drugs){
           for $patient in $k/p:Patient
           return if($patient/p:patient-id eq $id)
                    then <Patient-Drugs>
                            <patient-id>{$patient/p:patient-id/text()}</patient-id>
                            <name>{$patient/p:name/text()}</name>
                            <dob>{$patient/p:dob/text()}</dob>
                            <drug-treatments>{
                                for $drugTreatment in $patient/p:treatments/p:treatment/t:drug-treatment
                                return <drug-treatment>
                                             <diagnosis>{$drugTreatment/../t:diagnosis/text()}</diagnosis>
                                             <name>{$drugTreatment/t:name/text()}</name>
                                             <dosage>{$drugTreatment/t:dosage/text()}</dosage>
                                        </drug-treatment>
                            }</drug-treatments>
                            </Patient-Drugs>
                  else ()
    };

declare function g:getTreatmentInfo ($DB as element(c:Clinic)?)
as element(treatmentInfo){
<treatmentInfo>
{
    for $trm in $DB/p:Patient/p:treatments/p:treatment
        let $pid :=$trm/../../p:patient-id/text()
    return  
        <treatment>
             <patient-id>{$pid}</patient-id>
             <provider-id>{$trm/t:provider-id/text()}</provider-id>
             <diagnosis>{$trm/t:diagnosis/text()}</diagnosis>
             { 
              let $trmNode := $trm/*[last()]
              return typeswitch($trmNode)
              case $trmNode as element(t:radiology) return 
                    <radiology>{
                        for $date in $trmNode/t:date
                        return <date>{$date/text()}</date>
                    }</radiology>
              case $trmNode as element(t:surgery) return 
                    <surgery>{$trmNode/t:date/text()}</surgery>
              case $trmNode as element(t:drug-treatment) return 
                    <drug-treatment>
                    <name>{$trmNode/t:name/text()}</name>
                    <dosage>{$trmNode/t:dosage/text()}</dosage>
                    </drug-treatment>
              default
              return error("unknown element!")
             }
        </treatment>
}
</treatmentInfo>
};

declare function g:getProviderInfo($DB as element(c:Clinic)?)
as element(Providers){
<Providers>{
    for $pro-id in $DB/pr:Provider/pr:provider-id
    return <Provider>
                <provider-id>{$pro-id/text()}</provider-id>
                <patients>{
                    for $patient in $DB/p:Patient
                    return if($patient/p:treatments/p:treatment[t:provider-id = $pro-id])
                          then <patient>
                                <patient-id>{$patient/p:patient-id/text()}</patient-id>
                                <name>{$patient/p:name/text()}</name>
                                <dob>{$patient/p:dob/text()}</dob>
                                <treatments>
                                { 
                                    for $trm in $patient/p:treatments/p:treatment
                                        let $trmNode := $trm/*[last()]
                                    return if($trm[t:provider-id = $pro-id])
                                        then <treatment><diagnosis>{$trm/t:diagnosis/text()}</diagnosis>{
                                                typeswitch($trmNode)
                                                case $trmNode as element(t:radiology) return 
                                                    <radiology>{
                                                        for $date in $trmNode/t:date
                                                            return <date>{$date/text()}</date>
                                                                }</radiology>
                                                case $trmNode as element(t:surgery) return 
                                                    <surgery>{$trmNode/t:date/text()}</surgery>
                                                case $trmNode as element(t:drug-treatment) return 
                                                    <drug-treatment>
                                                    <name>{$trmNode/t:name/text()}</name>
                                                    <dosage>{$trmNode/t:dosage/text()}</dosage>
                                                    </drug-treatment>
                                                default
                                                return error("unknown element!")
                                             }</treatment>       
                                          else ()                             
                                }</treatments>
                          </patient>  
                      else ()                      
                }</patients>
           </Provider>
}</Providers>
};

declare function g:getDrugInfo($DB as element(c:Clinic)?)
as element(DrugInfo){<DrugInfo>{
    for $drugName in distinct-values($DB//t:name)
    return <drug>
             <name>{$drugName}</name>
             <treatments>
                {
                    for $drugTrmt in $DB//t:drug-treatment[t:name/text() = $drugName]
                        let $provider:= $DB/pr:Provider[pr:provider-id = $drugTrmt/../t:provider-id]
                     return 
                    <treatment>
                        <patient>
                            <patient-id>{$drugTrmt/../../../p:patient-id/text()}</patient-id>
                            <patient-name>{$drugTrmt/../../../p:name/text()}</patient-name>
                            <dob>{$drugTrmt/../../../p:dob/text()}</dob>
                        </patient>
                        <diagnosis>
                            {$drugTrmt/../t:diagnosis/text()}
                        </diagnosis>
                        <provider>
                            <provider-id>
                                {$provider/pr:provider-id/text()}
                            </provider-id> 
                            <provider-name>
                                {$provider/pr:name/text()}
                            </provider-name> 
                            <spec> {$provider/pr:spec/text()}</spec>                          
                        </provider>
                   </treatment>
                 }
             </treatments>
        </drug>
}</DrugInfo>    
};

