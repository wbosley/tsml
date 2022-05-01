package ml_6002b_coursework;

import java.util.Arrays;

/**
 * Empty class for Part 2.1 of the coursework.
 */
public class AttributeMeasures {

    /**
     * Takes a two dimensional array of integers as an argument and return a double.
     * Rows represent different values of the attribute being assessed,
     * Columns are the class counts.
     *
     * edge case: 0log(0) is undefined. define this as zero.
     *
     * @param data
     * @return
     */
    public static double measureInformationGain(int[][] data){

        //for now, just getting working out entropy of root.

        //the matrix would look like
        /*
            [[4,0],
             [1,5]]
         */

        //creating an array of zeros of length rowLength that will be used to keep track of the total values of each column.
        int rowLength = data[0].length;
        int columnLength = data.length;
        int[] columnTotals = new int[rowLength];
        Arrays.fill(columnTotals, 0);

        double[] columnProbablities = new double[rowLength];
        Arrays.fill(columnProbablities, 0.0);


        //inspecting each row
        for(int i = 0; i < data.length; i++){

            //inspecting each element in row
            for(int j = 0; j < rowLength; j++){
                //increase the total for this column.
                columnTotals[j] += data[i][j];
            }

        }

        int totalCount = 0; //calculating the total number of class counts
        for(int i = 0; i < columnTotals.length; i++){
            totalCount+= columnTotals[i];
        }
        System.out.println("total count: "+ totalCount);
        //calculating the probabilities for each class value
        for(int i = 0; i < columnTotals.length; i++){
            columnProbablities[i] += ((double)columnTotals[i] / (double)totalCount);
        }



        //calculating entropy of root

        //no simple way of changing the base of a log in java going to do this instead:
        //log2(B) == log10(B) / log10(2)

        double totalOfLogs = 0.0;
        for(int i = 0; i < columnProbablities.length; i++){
            double prob = columnProbablities[i];


            if(prob == 0.0){
                //accounting for if probability is zero. (0log2(0) is undefined, but for calculating entropy we call define it as zero)
                totalOfLogs += 0.0;
            } else {
                //totalOfLogs += prob * log2(prob)
                totalOfLogs += ( prob * (Math.log(prob) / Math.log(2)) );
            }


        }
        double rootEntropy = -totalOfLogs;
        System.out.println("entropy of root: " + rootEntropy);

        //now need to calculate the entropies of each row.

        double[] rowTotals = new double[columnLength];
        Arrays.fill(rowTotals, 0.0);

        //getting the total size of each row, and storing them in rowTotals
        //inspecting each row
        for(int i = 0; i < data.length; i++){

            //inspecting each element of each row
            for(int j = 0; j < data[i].length; j++){
                rowTotals[i] += data[i][j];
            }
        }

        //getting the probabilities of each of the class values for each attribute value.

        double[][] dataValueProbs = new double[columnLength][rowLength];
        for(int i = 0; i < dataValueProbs.length; i++){
            Arrays.fill(dataValueProbs[i], 0.0);
        }

        //inspecting each row
        for(int i = 0; i < data.length; i++){
            //inspecting each element of row
            for(int j = 0; j < data[i].length; j++){
                dataValueProbs[i][j] += ((double)data[i][j] / (double)rowTotals[i]);
            }
        }

        //now we have a table of probebilities.
        //now we need to get entopies of each attribute value:

        //no simple way of changing the base of a log in java going to do this instead:
        //log2(B) == log10(B) / log10(2)

        double[] rowEntropies = new double[columnLength];
        Arrays.fill(rowEntropies, 0.0);


        //looping through rows of dataVlueProbs
        for(int k = 0; k< dataValueProbs.length; k ++){
            totalOfLogs = 0.0;
            double[] probsThatMakeEntropy = dataValueProbs[k];

            for(int i = 0; i < probsThatMakeEntropy.length; i++){
                double prob = probsThatMakeEntropy[i];


                if(prob == 0.0){
                    //accounting for if probability is zero. (0log2(0) is undefined, but for calculating entropy we call define it as zero)
                    totalOfLogs += 0.0;
                } else {
                    //totalOfLogs += prob * log2(prob)
                    totalOfLogs += ( prob * (Math.log(prob) / Math.log(2)) );
                }


            }
            rowEntropies[k] = -totalOfLogs;
        }

        //we have calculated the entropies of each row, and the entropy of the node that the split was on. Now we need to combine these to get the gain.
        //totalCount - the total number of class values
        //rowTotals  - the number of class values per row

        double gainInProgress = rootEntropy;

        for(int i = 0; i < rowEntropies.length; i++){
            gainInProgress -= ((rowTotals[i] / totalCount) * (rowEntropies[i]));
        }

        double informationGain = gainInProgress;
        return informationGain;

    }

    public static double measureInformationGainRatio(int[][] data){

        //information gain ratio = split info / information gain

        int rowLength = data[0].length;
        int columnLength = data.length;

        // calculating split info
        double[] rowTotals = new double[columnLength];
        Arrays.fill(rowTotals, 0.0);

        //getting the total size of each row, and storing them in rowTotals
        //inspecting each row
        for(int i = 0; i < data.length; i++){

            //inspecting each element of each row
            for(int j = 0; j < data[i].length; j++){
                rowTotals[i] += data[i][j];
            }
        }

        int totalCount = 0;
        for(int i = 0; i < rowTotals.length; i++){
            totalCount+= rowTotals[i];
        }

        double[] rowProbabilities = new double[columnLength];
        Arrays.fill(rowProbabilities, 0.0);

        for(int i = 0; i < rowTotals.length; i++){
            rowProbabilities[i] = ((double)rowTotals[i] / (double)totalCount);
        }

        double totalOfLogs = 0.0;
        for(int i = 0; i < rowProbabilities.length; i++){
            double fraction = rowProbabilities[i];


            if(fraction == 0.0){
                //accounting for if probability is zero. (0log2(0) is undefined, but for calculating entropy we call define it as zero)
                totalOfLogs += 0.0;
            } else {
                //totalOfLogs += prob * log2(prob)
                totalOfLogs += ( fraction * (Math.log(fraction) / Math.log(2)) );
            }


        }
        double splitInfo = -totalOfLogs; //split info is correct as it aligns with slide show values. So if theres any incorrect answers, is probaly in th next bit:

        double gain = measureInformationGain(data);

        double gainRatio = (gain / splitInfo);
        System.out.println("splitInfo: " + splitInfo);
        return gainRatio;



    }

    public static double measureChiSquared(int[][] data){

        //creating an array of zeros of length rowLength that will be used to keep track of the total values of each column.
        int rowLength = data[0].length;
        int columnLength = data.length;
        int[] columnTotals = new int[rowLength];
        Arrays.fill(columnTotals, 0);

        double[] columnProbablities = new double[rowLength];
        Arrays.fill(columnProbablities, 0.0);


        //inspecting each row
        for(int i = 0; i < data.length; i++){

            //inspecting each element in row
            for(int j = 0; j < rowLength; j++){
                //increase the total for this column.
                columnTotals[j] += data[i][j];
            }

        }

        int totalCount = 0; //calculating the total number of class counts
        for(int i = 0; i < columnTotals.length; i++){
            totalCount+= columnTotals[i];
        }

        System.out.println("total count: "+ totalCount);

        //calculating the probabilities for each class value
        for(int i = 0; i < columnTotals.length; i++){
            columnProbablities[i] += ((double)columnTotals[i] / (double)totalCount);
            //System.out.println(columnProbablities[i]);
        }

        double[] rowTotals = new double[columnLength];
        Arrays.fill(rowTotals, 0.0);

        //getting the total size of each row, and storing them in rowTotals
        //inspecting each row
        for(int i = 0; i < data.length; i++){

            //inspecting each element of each row
            for(int j = 0; j < data[i].length; j++){
                rowTotals[i] += data[i][j];
            }
            //System.out.println(rowTotals[i]);
        }

        //now we need to multiply the row totals by the probailities to get the predicted number of class values for each option in the contingency table

        //creating a2d array populated with zeroes to store the numbers:
        double[][] predictedValues = new double[columnLength][rowLength];
        for(int i = 0; i < predictedValues.length; i++){
            Arrays.fill(predictedValues[i], 0.0);
        }

        //looping through the row totals
        for(int i = 0; i < rowTotals.length; i++){

            //looping through the column probabilities
            for(int j = 0; j < columnProbablities.length; j++){
                predictedValues[i][j] = rowTotals[i] * columnProbablities[j];//if theres an error, it may be here if im getting confused.
            }
            //System.out.println(Arrays.toString(predictedValues[i]));

        }

        //we now have the predicted value for each possible attribute/class value combination. USing these and the original data, we can calculate the chi squared statistic.

        double chiSquaredTotal = 0;

        //looping through rows
        for(int i = 0; i < data.length; i++){

            //looping through each element in the row
            for(int j = 0; j < data[i].length; j++){

                double observed = data[i][j];
                double expected = predictedValues[i][j];

                /*
                Chisquared total += (observed - expected) ** 2
                                    ---------------------------
                                            expected
                 */
                chiSquaredTotal += ( Math.pow((observed - expected), 2)/ expected );
            }
        }

        double chiSquared = chiSquaredTotal;
        return chiSquared;





    }

    public static double measureGini(int[][] data){
        //creating an array of zeros of length rowLength that will be used to keep track of the total values of each column.
        int rowLength = data[0].length;
        int columnLength = data.length;
        int[] columnTotals = new int[rowLength];
        Arrays.fill(columnTotals, 0);

        double[] columnProbablities = new double[rowLength];
        Arrays.fill(columnProbablities, 0.0);


        //inspecting each row
        for(int i = 0; i < data.length; i++){

            //inspecting each element in row
            for(int j = 0; j < rowLength; j++){
                //increase the total for this column.
                columnTotals[j] += data[i][j];
            }

        }

        int totalCount = 0; //calculating the total number of class counts
        for(int i = 0; i < columnTotals.length; i++){
            totalCount+= columnTotals[i];
        }
        System.out.println("total count: "+ totalCount);
        //calculating the probabilities for each class value
        for(int i = 0; i < columnTotals.length; i++){
            columnProbablities[i] += ((double)columnTotals[i] / (double)totalCount);
        }
        System.out.println(Arrays.toString(columnProbablities));

        //rootImpurity starts as 1, before we subtract the squares of the probabilities.
        double rootImpurity = 1;
        double toBeRemovedFromImpurity = 0;

        for(int i = 0; i < columnProbablities.length; i++){
            toBeRemovedFromImpurity += (Math.pow(columnProbablities[i], 2));
        }

        rootImpurity = rootImpurity - toBeRemovedFromImpurity;

        System.out.println(rootImpurity);

        //now we need to work out the impurity of each row.
        double[] rowTotals = new double[columnLength];
        Arrays.fill(rowTotals, 0.0);

        //getting the total size of each row, and storing them in rowTotals
        //inspecting each row
        for(int i = 0; i < data.length; i++){

            //inspecting each element of each row
            for(int j = 0; j < data[i].length; j++){
                rowTotals[i] += data[i][j];
            }
        }

        //getting the probabilities of each of the class values for each attribute value.

        double[][] dataValueProbs = new double[columnLength][rowLength];
        for(int i = 0; i < dataValueProbs.length; i++){
            Arrays.fill(dataValueProbs[i], 0.0);
        }

        //inspecting each row
        for(int i = 0; i < data.length; i++){
            //inspecting each element of row
            for(int j = 0; j < data[i].length; j++){
                dataValueProbs[i][j] += ((double)data[i][j] / (double)rowTotals[i]);
            }
        }

        //now we have a table of probebilities.

        double[] rowImpurities = new double[columnLength];
        Arrays.fill(rowImpurities, 0.0);

        for(int i = 0; i < dataValueProbs.length; i++) {
            System.out.println("data Value probs: " + Arrays.toString(dataValueProbs[i]));
        }

        for(int i = 0; i < dataValueProbs.length; i++){

            double rowImpurity = 1;
            toBeRemovedFromImpurity = 0;

            for(int j = 0; j < dataValueProbs[i].length; j++){
                toBeRemovedFromImpurity += (Math.pow(dataValueProbs[i][j], 2));
            }

            rowImpurity = rowImpurity - toBeRemovedFromImpurity;
            rowImpurities[i] = rowImpurity;


        }

        double gini = rootImpurity; //we start the gini impurity with the value of the roorts impurity, and then subtract the weighted impurities of the rows.

        System.out.println("row impurities:: " + Arrays.toString(rowImpurities));

        for(int i = 0; i < rowImpurities.length; i++){
            gini -= ( (rowTotals[i] / totalCount)* rowImpurities[i]);
        }

        return gini;

    }
    /**
     * Main method.
     *
     * @param args the options for the attribute measure main
     */
    public static void main(String[] args) {
        System.out.println("Not Implemented.");

        /*int[][] theData = {
                {4,0},
                {1,5}
        };*/
        int[][] theData = {
                {7,1},
                {1,3}
        };


        //measureGini(theData);
        double answer = measureGini(theData);
        System.out.println("gini: " + answer);



        //System.out.println(Math.log(27) / Math.log(3));


        /*int[][] theData2 = {
                {4,3},
                {1,5}
        };

        double answer2 = measureInformationGain(theData2);
        System.out.println("gain2: " + answer2);


        *//*int[][] theData3 = {
                {9,0},
                {0,5}
        };*//*
        int[][] theData3 = {
                {4,0},
                {0,5},
                {5,0}
        };

        double answer3 = measureInformationGainRatio(theData3);
        System.out.println("gain ratio3: " + answer3);*/

    }

}
