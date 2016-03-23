import xml.etree.ElementTree as ET
import re

class MapReduce:
    def __init__(self):
        self.intermediate = {}
        self.result = []

    def emit_intermediate(self, key, value):
        self.intermediate.setdefault(key, [])
        self.intermediate[key].append(value)

    def emit(self, value):
        self.result.append(value) 

    def execute(self, data, mapper, reducer):
        totData=""
        totDataEncode=totData.encode('ascii','ignore')
        for line in data:
            lineEncode=line.encode('ascii','ignore')
            totDataEncode=totDataEncode+lineEncode
        dataStr= totDataEncode.decode('ascii')
        rs=re.compile("&#([0-9]+);|&#x([0-9a-fA-F]+);")
        endpos= len(dataStr)
        pos=0
        list={""}
        while (pos < endpos) :
           there=rs.search(dataStr, pos)
           if not there: break
           st, en = there.span()
           pos=en
           list.add(there.group())
        list.remove("")
        for e in list:
           dataStr=dataStr.replace(e,"")
        while (dataStr.find("</REUTERS>") > 0):
           n=dataStr.find("</REUTERS>")+10
           newstr=dataStr[0:n]
           dataStr=dataStr[n+1:]
           root=ET.fromstring(newstr)
           txt=root.find('TEXT')
           if txt is not None:
              dateline=txt.find('DATELINE')
              if dateline is not None:
                 if(dateline.text.find("WASHINGTON") > 0):
                    title=txt.find('TITLE')
                    if title is not None:
                       mapper(title.text)

        for key in self.intermediate:
            reducer(key, self.intermediate[key])

