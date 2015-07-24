*Generate tables and sequences from this groovy project

* Export packages, functions, procs, types from PL/Dev. 

There are some trial code in this groovy project but no successfull result yet due to the lack of API?


Features
----------
- Extracts db objects from an Oracle database schema
- Extracts table create scripts and coments keeping the order of columns.
- Extracts indexes and constraints
- Extracts sequences


TODO
-----

- Compare with files from an existing folder, and override if changed
- Give dbexport report. 
      - list of added
      - list of items to be removed
      - list of items updated
      - list of items skipped
 - Skip pattens 
