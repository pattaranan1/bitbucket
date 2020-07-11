#!/usr/bin/env python3

import os
import time,datetime
# Import modules for CGI handling 
import cgi, cgitb 
import shutil


# Create instance of FieldStorage 
link = cgi.FieldStorage()

# Get data from fields

current_path = link.getvalue('link')
find_path = link.getvalue('find')
remove_file = link.getvalue('remove')

rename_file = link.getvalue('rename')
newname_file = link.getvalue('newname')

head = current_path if(current_path != None) else ""
checked_path = "?link="+current_path if(current_path != None) else ""

back_path = head.replace(head.split("/")[-1],"")

def rreplace(s, old, new):
  return (s[::-1].replace(old[::-1],new[::-1], 1))[::-1]

back_path = rreplace(back_path,"/","")
back_path = "?link=" + back_path if (back_path != "") else ""

def main():

  print ("Content-type:text/html\r\n\r\n")
  print ("<html>")
  print('<h1>Directory listing /{}</h1>'.format(head))
  print("<hr>")
  print("<head>")
  print('''
    <style style="text/css">
      .hoverTable{
      width:100%; 
      border-spacing: 0;
      border: 1px solid #ddd
    
    }
    .hoverTable th{
      text-align: left;
      padding: 16px;
    }
    .hoverTable td{
      text-align: left; 
      padding:5px; 
    }
 
    /* Define the default color for all the table rows */
    .hoverTable tr:nth-child(even) {
      background-color: #f2f2f2
    } 
    .hoverTable tr:not(:first-child):hover {
      background-color: #e6e6ff;
    }
    th {
    cursor: pointer;
    }
    .hoverTable th:hover{
      background-color: #e6e6ff;
    }
    </style>

  ''')
  print("</head>")
  print ("<body>")

  #print("{}".format(os.getcwd()))

  print('''<form action = "/cgi-bin/test2.py" method = "post">
      Find Directory/File: <input type = "text" name = "find" onkeyup="searchThing() "id="find" placeholder="search...">    
      <input type = "submit" value = "Search" />
    </form> ''')



  print('''<script>

  function searchThing(){
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("find");
    filter = input.value.toUpperCase();
    table = document.getElementById("myTable");
    tr = table.getElementsByTagName("tr");

    // Loop through all table rows, and hide those who don't match the search query
    for (i = 0; i < tr.length; i++) {
      td = tr[i].getElementsByTagName("td")[0];
      if (td) {
        txtValue = td.textContent || td.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
          tr[i].style.display = "";
      } else {
          tr[i].style.display = "none";
        }
      }
    }
  }
  
  </script>''')

  
  print('''<script>
  
    function hiddenFilefilter(){

      // Declare variables
      var table = document.getElementById("myTable");
      var tr = table.getElementsByTagName("tr");
      var checkbox = document.getElementById("hidden");
      // Loop through all table rows, and hide those who don't match the search query
      
      for (var i = 0; i < tr.length; i++) {
        var td = tr[i].getElementsByTagName("td")[0];
        if (td) {
          var txtValue = td.textContent || td.innerText;
          if (txtValue[0] == "." && !checkbox.checked) {
            tr[i].style.display = "none";
        } else {
            tr[i].style.display = "";
        }
      }
    }
  }

  </script>''')

  print('''<script>

    function sortByName(n){
      var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
      table = document.getElementById("myTable");
      switching = true;
      
      dir = "asc"; 
    

      while (switching) {

        switching = false;
        rows = table.rows;
     
        for (i = 1; i < (rows.length - 1); i++) {
         
          shouldSwitch = false;
          

          x = rows[i].getElementsByTagName("td")[n];
          y = rows[i + 1].getElementsByTagName("td")[n];
          
       

          if (dir == "asc") {
            if (x.innerText.toLowerCase() > y.innerText.toLowerCase()) {
              //if so, mark as a switch and break the loop:
              shouldSwitch= true;
              break;
            }
          } else if (dir == "desc") {
            if (x.innerText.toLowerCase() < y.innerText.toLowerCase()) {
              //if so, mark as a switch and break the loop:
              shouldSwitch = true;
              break;
            }
          }
        }

      
        
        if (shouldSwitch) {
          
          
          rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
          switching = true;
          
          switchcount ++;      
        } else {
        
          if (switchcount == 0 && dir == "asc") {
            dir = "desc";
            switching = true;
          }
        }
      }
    }
  </script>''')

  print('''<script>

    function sortBySize(){
      var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
      table = document.getElementById("myTable");
      switching = true;
      
      dir = "asc"; 
    

      while (switching) {

        switching = false;
        rows = table.rows;
     
        for (i = 1; i < (rows.length - 1); i++) {
         
          shouldSwitch = false;
          

          x = rows[i].getElementsByTagName("td")[3];
          y = rows[i + 1].getElementsByTagName("td")[3];

       

          if (dir == "asc") {
            if (parseFloat(x.attributes[0].value) < parseFloat(y.attributes[0].value)) {
              //if so, mark as a switch and break the loop:
              shouldSwitch= true;
              break;
            }
          } else if (dir == "desc") {
            if (parseFloat(x.attributes[0].value) > parseFloat(y.attributes[0].value)) {
              //if so, mark as a switch and break the loop:
              shouldSwitch = true;
              break;
            }
          }
        }

        if (shouldSwitch) {
          
          
          rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
          switching = true;
          
          switchcount ++;      
        } else {
        
          if (switchcount == 0 && dir == "asc") {
            dir = "desc";
            switching = true;
          }
        }
      }
    }

   
  </script>''')

  print('''<script>

    function sortByDate(){
      var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
      table = document.getElementById("myTable");
      switching = true;
      
      dir = "asc"; 
    

      while (switching) {

        switching = false;
        rows = table.rows;
     
        for (i = 1; i < (rows.length - 1); i++) {
         
          shouldSwitch = false;
          

          x = rows[i].getElementsByTagName("td")[1];
          y = rows[i + 1].getElementsByTagName("td")[1];

       

          if (dir == "asc") {
            if (parseFloat(x.attributes[0].value) < parseFloat(y.attributes[0].value)) {
              //if so, mark as a switch and break the loop:
              shouldSwitch= true;
              break;
            }
          } else if (dir == "desc") {
            if (parseFloat(x.attributes[0].value) > parseFloat(y.attributes[0].value)) {
              //if so, mark as a switch and break the loop:
              shouldSwitch = true;
              break;
            }
          }
        }

        if (shouldSwitch) {
          
          
          rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
          switching = true;
          
          switchcount ++;      
        } else {
        
          if (switchcount == 0 && dir == "asc") {
            dir = "desc";
            switching = true;
          }
        }
      }
    }
  </script>''')

  

  print('''<form action = "/cgi-bin/test2.py{}" method = "post">
      Remove Directory/File in Current Directory: <input type = "text" id="remove" name = "remove">    
      <input type = "submit" value = "Delete"/>
    </form> '''.format(checked_path))

  print('''<script>

  function printCheckBoxes(){
    var textbox = document.getElementById("remove");
    textbox.value = "";
    var checkboxes = document.querySelectorAll('input[type=checkbox]:checked');
		for (var i = 0; i < checkboxes.length; i++) {
      textbox.value += checkboxes[i].value;
      if(i != checkboxes.length-1){
        textbox.value += ",";
      }
    }
    

  }
  </script>''')


  print('''<form action = "/cgi-bin/test2.py{}" method = "post">
      Rename Directory/File from <input type = "text" name = "rename">
      to: <input type = "text" name="newname"/> 
      <input type = "submit" value = "Rename" />
    </form> '''.format(checked_path))


  print('''
  <form action = "/cgi-bin/test2.py{}" method = "post">
  <input type = "submit" value = "<< back" />
  </form> 
  <input type ="checkbox" id="hidden" name="hidden" value="hidden" onchange="hiddenFilefilter()">Show hidden files   
  '''.format(back_path))

  print('<br><br>')

  print('''
  <table id="myTable" class="hoverTable">
  <tr class="header">
    <th onclick="sortByName(0)" style="width:40%;">Name</th>
    <th onclick="sortByDate()" style="width:20%;">Date Modified</th>
    <th onclick="sortByName(2)" style="width:20%;">Type</th>
    <th onclick="sortBySize()" style="width:20%;">Size</th>
  </tr>''') 

  toRemove()
  toRename()
  directoryListingOrSearching()

  print('''<script>
  hiddenFilefilter()
  </script>''')

  print("</table>")
  print ("</body>")
  print ("</html>") 

  
def remove(rm_file,path=""):
  files = rm_file.split(",")
  for rm in files:
    remove_file = path+rm.strip()
    try: 
      if(os.path.isdir(remove_file)):
        shutil.rmtree(remove_file)
      else:
        os.remove(remove_file) 
    except OSError as error: 
      print(error) 
      print("File path can not be removed") 
      print("<br>")

def toRemove():
  if(remove_file != None): #Delete
    if(current_path == None): 
      remove(remove_file)
    else:
      remove(remove_file,current_path+"/")


def rename(path = "."):
  p = "" if(path == ".") else path + "/"
  for files in os.listdir(path):
      if(rename_file == files):
        os.rename(p + rename_file, p + newname_file)
        break

def toRename():
  if(rename_file != None and newname_file != None): #Rename
    if(current_path == None):
      rename()
    else:
      rename(current_path)

def convert_bytes(num):
    """
    this function will convert bytes to MB.... GB... etc
    """
    for x in ['bytes', 'KB', 'MB', 'GB', 'TB']:
        if num < 1000.0:
            return f'{num:.1f} {x}'
        num /= 1000.0

def isDirectoryOrFile(files,path=""):

  fileinfo = os.stat(path+files)
  filedate = datetime.datetime.fromtimestamp(fileinfo.st_mtime).strftime('%m/%d/%Y %I:%M %p')
  if(os.path.isdir(path+files)):
    filesize = convert_bytes(fileinfo.st_size)
    print("<tr>")
    print('''<td><a href="/cgi-bin/test2.py?link={}"><input name="check" type="checkbox" onclick="printCheckBoxes()" value ="{}"/>{}</a></td>'''.format(path + files,files,files))
    print('''<td value = "{}">{}</td>'''.format(fileinfo.st_mtime,filedate))
    print('''<td>File Directory</td>''')
    print('''<td value ="0"></td>''')
    print("</tr>")
    
  else:
    filesize = convert_bytes(fileinfo.st_size)
    print("<tr>")
    print('''<td><a href="{}" target="_blank"><input name="check" type="checkbox" onclick="printCheckBoxes()" value ="{}"/>{}</a></td>'''.format("../" + path + files,files,files))
    print('''<td value = "{}">{}</td>'''.format(fileinfo.st_mtime,filedate))
    print('''<td>File</td>''')
    print('''<td value ="{}">{}</td>'''.format(fileinfo.st_size,filesize))
    print("</tr>")
   
def directoryListingOrSearching():
  if(find_path != None): #Searching 

    print("<p>You are searching for {}</p>".format(find_path))

    for r,d,f in os.walk("."):
      for file in f+d:
        if(find_path.lower() in file.lower()):
          found_path = os.path.join(r.replace("./","") ,file)  
          isDirectoryOrFile(found_path)

  elif(current_path == None): #Directory listing when None value in current path 
    for files in os.listdir("."):
      isDirectoryOrFile(files)

  else: #Directory listing on current path
    for files in os.listdir(current_path):
      isDirectoryOrFile(files,current_path+"/")
      
main()