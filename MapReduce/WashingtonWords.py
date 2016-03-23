import MapReduce
import sys
import string
import operator

AllWords = {}
DocWords = {}

mr = MapReduce.MapReduce()

def mapper(record):
     stopwords={"A", "ABOUT", "ABOVE", "ABOVE", "ACROSS", "AFTER", "AFTERWARDS", "AGAIN", "AGAINST", 
                "ALL", "ALMOST", "ALONE", "ALONG", "ALREADY", "ALSO", "ALTHOUGH", "ALWAYS", "AM", 
                "AMONG", "AMONGST", "AMOUNGST", "AMOUNT", "AN", "AND", "ANOTHER", "ANY", "ANYHOW", 
                "ANYONE", "ANYTHING", "ANYWAY", "ANYWHERE", "ARE", "AROUND", "AS", "AT", "BACK", 
                "BE", "BECAME", "BECAUSE", "BECOME", "BECOMES", "BECOMING", "BEEN", "BEFORE", 
                "BEFOREHAND", "BEHIND", "BEING", "BELOW", "BESIDE", "BESIDES", "BETWEEN", "BEYOND", 
                "BILL", "BOTH", "BOTTOM", "BUT", "BY", "CALL", "CAN", "CANNOT", "CANT", "CO", "CON", 
                "COULD", "COULDN'T", "CRY", "DE", "DESCRIBE", "DETAIL", "DO", "DONE", "DOWN", "DUE", 
                "DURING", "EACH", "EG", "EIGHT", "EITHER", "ELEVEN", "ELSE", "ELSEWHERE", "EMPTY", 
                "ENOUGH", "ETC", "EVEN", "EVER", "EVERY", "EVERYONE", "EVERYTHING", "EVERYWHERE", 
                "EXCEPT", "FEW", "FIFTEEN", "FIFTY", "FILL", "FIND", "FIRE", "FIRST", "FIVE", "FOR", 
                "FORMER", "FORMERLY", "FORTY", "FOUND", "FOUR", "FROM", "FRONT", "FULL", "FURTHER", "GET", 
                "GIVE", "GO", "HAD", "HAS", "HASN'T", "HAVE", "HE", "HENCE", "HER", "HERE", "HEREAFTER", 
                "HEREBY", "HEREIN", "HEREUPON", "HERS", "HERSELF", "HIM", "HIMSELF", "HIS", "HOW", "HOWEVER", 
                "HUNDRED", "IE", "IF", "IN", "INC", "INDEED", "INTEREST", "INTO", "IS", "IT", "ITS", "ITSELF", 
                "KEEP", "LAST", "LATTER", "LATTERLY", "LEAST", "LESS", "LTD", "MADE", "MANY", "MAY", "ME", 
                "MEANWHILE", "MIGHT", "MILL", "MINE", "MORE", "MOREOVER", "MOST", "MOSTLY", "MOVE", "MUCH", 
                "MUST", "MY", "MYSELF", "NAME", "NAMELY", "NEITHER", "NEVER", "NEVERTHELESS", "NEXT", "NINE", 
                "NO", "NOBODY", "NONE", "NOON", "NOR", "NOT", "NOTHING", "NOW", "NOWHERE", "OF", "OFF", "OFTEN", 
                "ON", "ONCE", "ONE", "ONLY", "ONTO", "OR", "OTHER", "OTHERS", "OTHERWISE", "OUR", "OURS", 
                "OURSELVES", "OUT", "OVER", "OWN", "PART", "PER", "PERHAPS", "PLEASE", "PUT", "RATHER", "RE", 
                "SAME", "SEE", "SEEM", "SEEMED", "SEEMING", "SEEMS", "SERIOUS", "SEVERAL", "SHE", "SHOULD", "SHOW", 
                "SIDE", "SINCE", "SINCERE", "SIX", "SIXTY", "SO", "SOME", "SOMEHOW", "SOMEONE", "SOMETHING", 
                "SOMETIME", "SOMETIMES", "SOMEWHERE", "STILL", "SUCH", "SYSTEM", "TAKE", "TEN", "THAN", "THAT", 
                "THE", "THEIR", "THEM", "THEMSELVES", "THEN", "THENCE", "THERE", "THEREAFTER", "THEREBY", "THEREFORE", 
                "THEREIN", "THEREUPON", "THESE", "THEY", "THICK", "THIN", "THIRD", "THIS", "THOSE", "THOUGH", "THREE", 
                "THROUGH", "THROUGHOUT", "THRU", "THUS", "TO", "TOGETHER", "TOO", "TOP", "TOWARD", "TOWARDS", "TWELVE", 
                "TWENTY", "TWO", "UN", "UNDER", "UNTIL", "UP", "UPON", "US", "VERY", "VIA", "WAS", "WE", "WELL", "WERE", 
                "WHAT", "WHATEVER", "WHEN", "WHENCE", "WHENEVER", "WHERE", "WHERE AFTER", "WHEREAS", "WHEREBY", "WHEREIN", 
                "WHEREUPON", "WHEREVER", "WHETHER", "WHICH", "WHILE", "WHITHER", "WHO", "WHOEVER", "WHOLE", "WHOM", "WHOSE",                "WHY", "WILL", "WITH", "WITHIN", "WITHOUT", "WOULD", "YET", "YOU", "YOUR", "YOURS", "YOURSELF",
                "YOURSELVES"}

     line = record
     wordList=line.split()
     for i, word in enumerate(wordList):
         if (word not in  stopwords):
             if(word.find("U.S.") > -1):
                word="U.S."
             else:
                punc=set(string.punctuation)
                punc.remove("-")
                word=''.join(ch for ch in word if ch not in punc)
             if not word.isnumeric():
                mr.emit_intermediate(word,1)
      
def reducer(key, list_of_values):
     #key: word
     #value: list of occurrence counts
     total = 0
     for v in list_of_values:
       total += v
     DocWords [key] = total

if __name__ == '__main__':

  for filename in sys.argv[1:]:
      inputdata=open(filename,encoding='latin1')
      mr.execute(inputdata, mapper, reducer)
      for key,val in list(DocWords.items()):
          if key in AllWords:
             AllWords[key]=AllWords.get(key)+val;
          else:
            AllWords[key]=val
      DocWords={}
 
  sorted_words=sorted(AllWords.items(), key=operator.itemgetter(1), reverse=True)
   
  for i, val in enumerate(sorted_words):
        s=""+str(val)
        print (s[2:s.find(",")-1])
        if (i==9): break
