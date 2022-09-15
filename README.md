# Analysis .dat file System
Application for analysis and generate reports of .dat files in real time.

## Configuring
1. Clone this project
2. Update .env file according your paths
3. To generate reports from folder in real time, the folder should exist. So make sure your .env vars are correct
4. Put files to be analysed in the path of *app.folder.in.path* .env var, and the reports files will be generated in path of *app.folder.out.path*

## Running

- Start the application
  - If exists files in folder when application start, it will be reported. Don't worry.
  - For generate news reports, add new file/files to the folder

## Testing

### Complete report
- Create a .dat file with this content
  -        003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego002ç11111111111111çJose da Silva TesteçMarketing  001ç44444444444çGabriely Nehç40000.99002ç22222222222222çEduardo PereiraçVarejo 003ç08ç[1-34-10,2-33-15.50,3-40-0.10]çRenato   001ç32456788654çRenatoç40000.99
    - The output should be: **2ç2ç10çRenato**, because:
      1. Has 2 Customers with different CNPJs
      2. Has 2 Salesman with different CPFs
      3. If you sum: **[ (100×10) + (30×2,50) + (40×3,10) ] = 1199** (id 10) is more than **[ (34×10) + (33×15,50) + (40×0,10) ] = 855.50** (id 08)
      4. The name of the salesman who sold *855.50* is Renato.

### Uncompleted report

- Create a .dat file with this content
  -        001ç1234567891234çDiegoç50000 001ç3245678865434çRenatoç40000.99002ç2345675434544345çJose da SilvaçRural 002ç2345675433444345çEduardoPereiraçRural 003ç10ç[1-10-100,2-30-2.50,3-40-3.10]çDiego 111003ç08ç[1-34-10,2-33-1.50,3-40-0.10]çRenato
    - The output should be: **-1ç-1ç10çRenato**. The first and second is **-1** :
      - For the data in file be considered in the report, it has to pass by validation. In this case the CNPJ and CPF length is bigger than allowed and was ignored.
      - If the others fields don't pass by validation, will be ignored too and the report will fill the position with a generic value