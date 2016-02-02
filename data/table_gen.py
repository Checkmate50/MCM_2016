"""
Converts the provided .txt file into a '&' deliminated series of rows
Each of these lines is separated by a second given string
The result is read into a file titled name_table.txt where name is the file read from
Written by Dietrich Geisler
"""
	
f = input("Please give the name of the input file: ")
delim = input("Please give the string used to separate lines: ")

delim = delim.strip() + "\n"

file = open(f, 'r')
f = f.replace(".txt", "")
ofile = open(f+"_table.txt", 'w')

for s in file:
	split = s.replace("\n","\t").split("\t")	
	temp = ""
	for i in split:
		temp += i+"&"
	temp = temp.rstrip("&")+"\\\\\n"
	if delim != "\n":
		temp += delim
	ofile.write(temp)