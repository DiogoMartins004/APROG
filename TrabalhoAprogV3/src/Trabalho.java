import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Trabalho {
    static final String FILE = "input.txt";
    static final int LIMIT = 300;
    static final int TEXTLINES = 5;
    static final int ALTERACAO1 = -10;
    static final int ALTERACAO2 = 10;
    static final int MODERATE = 20;
    static final int HIGH = 30;
    static final int CATASTROPHIC = 40;
    static final int ELEMENTS = 15;
    static final int FIRE = 50;

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(FILE);

        // a)
        int[] arrRowsColumns = readDimensions(file);
        int rows = arrRowsColumns[0];
        int columns = arrRowsColumns[1];

        // b)
        System.out.println("b)");
        int[][] arrMT = makeMT(file,rows, columns);
        showMT(arrMT, rows, columns);

        // c)
        System.out.println();
        System.out.print("c)");
        String[][] arrMA = makeMA(arrMT, rows, columns);
        System.out.println();
        showMA(arrMA, rows, columns);

        //d)
        System.out.println();
        System.out.print("d)");
        System.out.println();
        int[][] arrNewMT = makeAlteracaoMT(arrMT,rows,columns, ALTERACAO1);
        showMT(arrNewMT,rows,columns);
        System.out.println();
        String[][] arrNewMA = makeMA(arrNewMT, rows, columns);
        showMA (arrNewMA,rows,columns);

        //e)
        System.out.println();
        System.out.print("e)");
        System.out.println();
        int counterModerate = countModerate(arrNewMA, rows, columns);
        int counterHigh = countHigh(arrNewMA, rows, columns);
        int counterExtreme = countExtreme(arrNewMA, rows, columns);
        int counterCatastrophic = countCatastrophic(arrNewMA, rows, columns);
        showArea(counterModerate, counterHigh, counterExtreme, counterCatastrophic);

        //f)
        System.out.println();
        System.out.print("f)");
        System.out.println();
        int lowestTemperature = findLowestTemperature(arrNewMT,rows,columns);
        showTemperatureToAllCatastrophic(lowestTemperature);

        //g)
        System.out.println();
        System.out.print("g)");
        System.out.println();
        // Aproveitámos o módulo já criado anteriormente para atualizar o MT com
        // a nova alteração na temperatura
        int[][] arrNewNewMT = makeAlteracaoMT(arrNewMT,rows,columns, ALTERACAO2);
        String[][] arrNewNewMA = makeMA(arrNewNewMT, rows, columns);
        showMA (arrNewNewMA,rows,columns);
        showChangesOnTheGround(arrNewNewMA, arrNewMA, rows, columns);
        System.out.println();

        // h)
        System.out.println();
        System.out.print("h)");
        System.out.println();
        String[][] arrNorteSulMA = makeNorteSulMA(arrNewNewMA,rows,columns);
        showMA(arrNorteSulMA,rows,columns);

        // i)
        System.out.println();
        System.out.println("i)");
        showMT(arrMT, rows, columns);
        dropWater(arrMT, rows, columns);

        // j)
        System.out.println();
        System.out.println("j)");
        int safestColumn = lookForSafeColumn(arrMA,rows,columns);
        showSafestColumn(safestColumn);
    }

    // a)

    /*
        Este módulo serve para ler o ficheiro, e de lá retirar as dimensões da matriz das
        temperaturas
     */

    public static int[] readDimensions(File file) throws FileNotFoundException {
        Scanner ler = new Scanner(file);
        int counter = 0;

        int[] arrDimensions = new int[2];

        String positionsLine = findLine(file, 2);

        if(!positionsLine.equals("")){
           /*
           como sabemos que as dimensões da matriz estão na segunda linha, através do split
           vamos colocar num array os elementos que nos interessam
            */
            for (String element : positionsLine.split(" ")) {
                // como ao retirar os elementos estes vêm como string temos que mudar estes para int
                arrDimensions[counter] = Integer.parseInt(element);
                counter ++;

            }
        }

        ler.close();

        return arrDimensions;
    }

    /*
    Criámos esta função para encontar-nos uma linha no ficheiro
     */

    public static String findLine(File file, int line) throws FileNotFoundException {
        Scanner ler = new Scanner(file);
        String[] arrLines = new String[LIMIT];
        int counter = 0;

        do{
            arrLines[counter] = ler.nextLine();
            counter ++;

        }while(ler.hasNextLine());

        return arrLines[line -1];
    }

    public static int[][] makeMT(File file, int rows, int columns) throws FileNotFoundException{
        Scanner ler = new Scanner(file);
        String line;
        int counterRows = 0;
        int counterColumns = 0;

        int[][]arrMT = new int[rows][columns];

        /*
        Vamos percorrer todas as linhas onde estão as temperaturas no ficheiro e vamos colocá-las
        numa matriz
        Sabemos ambém que no ficheiro as temperaturas começam a aparecer na linha 3 até um determinado
        número da linha, que corresponde também à última linha do ficheiro, que neste caso é a constante
        "TEXTLINES"
         */

        for (int i = 3; i <= TEXTLINES; i++) {
            /*
            Procurámos a linha, colocamos numa matriz, mas antes trocámos as string para int
             */
            line = findLine(file, i);

            for (String element : line.split(" ")){
                arrMT[counterRows][counterColumns] = Integer.parseInt(element);
                counterColumns++;

            }
            // Após ler-nos uma linha resetámos o contador das colunas e incrementamos o das linhas
            counterColumns = 0;
            counterRows++;

        }
        ler.close();

        return arrMT;
    }

    // b)
    // Visualizar o MT no ecrã, com os valores das colunas alinhados à direita


    public static void showMT (int[][] arrMT, int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.printf("%4d", arrMT[i][j]);
            }
            System.out.print("\n");
        }
    }

    // c)

    // Vamos agora criar a arrMA

    public static String[][] makeMA (int[][] arrMT, int rows, int columns) {
        String[][] arrMA = new String[rows][columns];
        /*
        Para fazer-nos a arrMA vamos percorrer toda a arrMA e verificar se a
        temperatura é:
         < 20 colocámos um "M" na arrMA, na mesma posição que na arrMT;
         20 < 30 colocámos um "H"
         30 < 40 colocámos um "E"
         >= 40 colocámos um "C"
         */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {

                if (arrMT[i][j] < MODERATE) {
                    arrMA[i][j] = "M";
                }
                if (arrMT[i][j] >= MODERATE && arrMT[i][j] < HIGH) {
                    arrMA[i][j] = "H";
                }
                if (arrMT[i][j] >= HIGH && arrMT[i][j] < CATASTROPHIC) {
                    arrMA[i][j] = "E";
                }
                if (arrMT[i][j] >= CATASTROPHIC) {
                    arrMA[i][j] = "C";
                }
            }
        }
        return arrMA;
    }

    // Criámos este módulo para mostrar-nos a arrMA
    public static void showMA(String[][] novaMA, int rows, int columns) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(novaMA[i][j]);
            }
            System.out.print("\n");
        }
    }

    // d)

    // Obter um novo MA para refletir uma qualquer alteração de temperatura (ex: -10ºC)

    // Este módulo obtem um novo MT com a alteração de temperatura
    public static int[][] makeAlteracaoMT (int[][] arrMT, int rows, int columns, int ALTERACAO) {
        int[][] novaMT = new int[rows][columns];

        /*
        Para colocar essa alteração na arrMT percurremos toda a matriz e alterámos
        temperatura uma a uma.
         */

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                novaMT[i][j] = arrMT[i][j] + ALTERACAO;
            }
        }
        return novaMT;
    }

    // e)

    //Criámos 4 módulos cada um para contar o respetivo número de cada tipo de aviso.

    public static int countModerate(String[][] arrNewMA, int rows, int columns){
        int counter = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(arrNewMA[i][j].equals("M")){
                    counter++;

                }
            }
        }
        return counter;
    }

    public static int countHigh(String[][] arrNewMA, int rows, int columns){
        int counter = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(arrNewMA[i][j].equals("H")){
                    counter++;

                }
            }
        }
        return counter;
    }

    public static int countExtreme(String[][] arrNewMA, int rows, int columns){
        int counter = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(arrNewMA[i][j].equals("E")){
                    counter++;

                }
            }
        }
        return counter;
    }

    public static int countCatastrophic(String[][] arrNewMA, int rows, int columns){
        int counter = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(arrNewMA[i][j].equals("C")){
                    counter++;

                }
            }
        }
        return counter;
    }

    // Fazemos agora um módulo para calcular e mostrar a percentagem de área para cada
    // dos alertas.

    public static void showArea(int counterModerate, int counterHigh, int counterExtreme, int counterCatastrophic) {
        double percentageModerate = (double) (counterModerate * 100) / ELEMENTS;
        double percentageHigh = (double) (counterHigh * 100) / ELEMENTS;
        double percentageExtreme = (double) (counterExtreme * 100) / ELEMENTS;
        double percentageCatastrophic = (double) (counterCatastrophic * 100) / ELEMENTS;

        System.out.printf("MODERATE     :%8.2f%%\n", percentageModerate);
        System.out.printf("HIGH         :%8.2f%%\n", percentageHigh);
        System.out.printf("EXTREME      :%8.2f%%\n", percentageExtreme);
        System.out.printf("CATASTROPHIC :%8.2f%%\n", percentageCatastrophic);
    }

    // f)
 
     /*
        Visualizar quantos ºC teria de subir a temperatura, para a totalidade do terreno ficar no nível de alerta
        CATASTROPHIC;
     */

    public static int findLowestTemperature (int[][] novaMT, int rows, int columns) {
        int lowestTemperature = novaMT[0][0];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if (novaMT[i][j] <= lowestTemperature) {
                    lowestTemperature = novaMT[i][j];
                }
            }
        }
        return lowestTemperature;
    }

    public static void showTemperatureToAllCatastrophic (int lowestTemperature) {
        int temperatureToCatastrophic;
        temperatureToCatastrophic = CATASTROPHIC - lowestTemperature;
        System.out.println("To get all terrain on CATASTROPHIC alert, the temperature has\n" + "to rise : " + temperatureToCatastrophic + " ºC");
    }

    // g)

    // Criámos este módulo para percorrer ambas a MA e através de um contador
    // contar o número de alertas diferentes.

    public static void showChangesOnTheGround(String[][] arrNewNewMA, String[][] arrNewMA, int rows, int columns) {
        int counter = 0;
        // Para poder-nos comparar ambos os MA, criámos duas variáveis.
        String alert1, alert2;
        double percentage;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                alert1 = arrNewMA[i][j];
                alert2 = arrNewNewMA[i][j];
                // Se o alert1 for diferente do alert2 significa que não ouve
                // alteração por isso o nosso contador não incrementa.
                if(!alert1.equals(alert2)) {
                    counter++;
                }
            }
        }

        // Como nos pedia a percentagem de terreno que sofreu alterações através
        // de uma constante "ELEMENTS" que é o número de elementos de ambas as matrizes
        // (MT e MA) calculámos então essa percentagem.

        percentage = (double) (counter * 100) / ELEMENTS;

        System.out.println();
        System.out.printf("Alerts Levels changes due to temperature variations by 10ºC :%6.2f%%", percentage);
    }

    // h)

    public static String[][] makeNorteSulMA (String[][] newNewMA,int rows, int columns)  {
        String[][] norteSulMA = new String[rows][columns];

        for (int i = 0; i < rows ; i++) {
            for (int j = 0; j < columns ; j++) {
                norteSulMA[i][j] = newNewMA[i][j];

                if (i > 0 && newNewMA[i-1][j].equals("C")) {
                    norteSulMA[i][j] = "C";
                }
            }
        }
        return norteSulMA;
    }

    // i)

    public static void dropWater(int[][] arrMT, int rows, int columns){
        int[] position = new int[2];
        int bigger = 0, rowDrop = 0, columnDrop = 0, fireCount;

        // Primeiro vamos verificar se existe fogos através do módulo "checkIfIsOnFire"

        if(checkIfIsOnFire(arrMT, rows, columns)){
                // Como já vimos que existem fogos, vamos então observar a quantidade de fogos
                // existentes num quadrado 3x3 (que é o alcance do helicóptero)

                // Como não adianta começar nem acabar na primeira ou última, linha ou coluna,
                // pois seria gastar água em vão, vamos começar os nossos ciclos uma linha e coluna
                // à frente e acabar uma atrás.
                for (int i = 1; i < (rows - 1); i++) {
                    for (int j = 1; j < (columns - 1); j++) {
                        position[0] = i;
                        position[1] = j;

                            // Como precisámos de saber o local com maior quantidade de fogos
                            // vamos criar um módulo para fazer isso
                            fireCount = countFireSpots(arrMT, position);

                            if (fireCount > bigger) {
                                bigger = fireCount;
                                rowDrop = i;
                                columnDrop = j;
                            }
                    }
                }
            System.out.println();
            System.out.println("drop water at " + "(" + rowDrop + "," + columnDrop + ")");

        }else{
            System.out.println("no fire");
        }
    }

    public static int countFireSpots(int[][] arrMT, int[] position){
        int squareSide = 3, row, column, counter = 0;
        int[] squarePosition = new int[2];

        squarePosition[0] = position[0] - 1;
        squarePosition[1] = position[1] -1;

        for (int i = 0; i < squareSide; i++) {
            row = squarePosition[0] + i;

            for (int j = 0; j < squareSide; j++) {
                column = squarePosition[1] + j;

                // Como sabemos que para estar ptencialmente a arder a tempratura
                // tem que estar acima de 50ºC, vamos verficar se em cada um dos pontos
                // do quadrado a temperatura é maior ou não que 50ºC, se for o contador de
                // fogos incrementa.
                if(arrMT[row][column] > FIRE) {
                    counter++;
                }
            }
        }

        return counter;
    }

    public static boolean checkIfIsOnFire(int[][] arrMT, int rows, int columns){

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                if(arrMT[i][j] > FIRE){
                    return true;
                }
            }
        }
        return false;
    }

    // j)

    public static int lookForSafeColumn (String[][] arrMA, int rows, int columns) {
        int safestColumn = -1;
        boolean notSafe = false;

        for (int i = (columns - 1); i >= 0; i--) {
            for (int j = 0; j < rows; j++) {
                if(arrMA[j][i].equals("C")){
                    notSafe = true;
                }

            }
            if(!notSafe){
            return i;
            }

            notSafe = false;
        }

        return -1;
    }

    public static void showSafestColumn(int safestColumn){
        if (safestColumn != -1) {
            System.out.println( "safe column = " + "(" + safestColumn + ")");

        } else {
            System.out.println("safe column =NONE" );

        }
    }
}