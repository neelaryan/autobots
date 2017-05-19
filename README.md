# autobots
Automate the boring stuff

1. **merge_nessus.py** - merge multiple .nessus files into one. must be run in the same folder as .nessus files to be merged. stores resulting file in *nss_report/report.nessus* (Source: unknown)

2. **Nessus2Report.java** - dump nessus information based on vulnerabilities (severity, vulnerability name, ports affected, description, solution, references, hosts affected, cve id's) into an html table format. can be redirected to .html / .xls files using > redirection operator. (Based on: https://github.com/JasonMOliver/Java_Parsers/blob/master/XMLTable.java)

3. **Nessus2Table.java** - dump nessus information based on vulnerabilities (severity, vulnerability name, ports affected, hosts affected) into an html table format. can be redirected to .html / .xls files using > redirection operator. (Based on: https://github.com/JasonMOliver/Java_Parsers/blob/master/XMLTable.java)
