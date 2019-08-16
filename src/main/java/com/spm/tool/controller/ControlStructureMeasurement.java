
package com.spm.tool.controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import com.spm.tool.model.*;


public class ControlStructureMeasurement {
	
	static ArrayList<String> lineList;
	public ControlStructureMeasurement(ArrayList<String> lines) {
		
		lineList = new ArrayList<>();
		this.lineList = lines;
	}

    public static ArrayList<StatementLine> calculateComplexityByType(ArrayList<Method> functionList) {

        int x;
        int y;
        int z;
        int a;
        int start;
        int end;
        int switchStart, switchEnd;
        int numberOfCases;
        String[] lineToChars;
        ArrayList<String> bracketCheckSwitch = new ArrayList<>();
        boolean isLinePresent;

        int Ctc;

        ArrayList<StatementLine> StatementList = new ArrayList<>();

        String[] divideBySpaces;
        if (functionList.size() == 0) {
            return null;
        }

        for (x = 0; x < functionList.size(); x++) {
            Method f1 = functionList.get(x);
            start = f1.getStartLine();
            end = f1.getEndLine();

            while (start != end + 1) {
                Ctc = 0;
                numberOfCases = 0;
                System.out.println("Line number " + start + " : " + lineList.get(start));

                if (lineList.get(start).contains("if") && lineList.get(start).contains("(") && lineList.get(start).contains(")")) {
                    System.out.println("Line number " + start + " has a if statement");
                    Ctc++;

                    divideBySpaces = lineList.get(start).split("\\s");
                    for (y = 0; y < divideBySpaces.length; y++) {
                    	System.out.println(divideBySpaces[y]);
                        if (divideBySpaces[y].contains("&&") || divideBySpaces[y].contains("||") || divideBySpaces[y].contains("&") || divideBySpaces[y].contains("|")) {
                            Ctc++;
                        }
                    }
                }

                if (lineList.get(start).contains("for") && lineList.get(start).contains("(") && lineList.get(start).contains(")") && lineList.get(start).contains("{")) {
                    System.out.println("Line number " + start + " has a for statement");
                    Ctc = Ctc + 2;

                    divideBySpaces = lineList.get(start).split("\\s");
                    for (y = 0; y < divideBySpaces.length; y++) {
                        if (divideBySpaces[y].contains("&&") || divideBySpaces[y].contains("||") || divideBySpaces[y].contains("&") || divideBySpaces[y].contains("|")) {
                            Ctc++;
                        }
                    }
                }

                if (lineList.get(start).contains("while") && lineList.get(start).contains("(") && lineList.get(start).contains(")") && lineList.get(start).contains("{")) {
                    System.out.println("Line number " + start + " has a While statement");
                    Ctc = Ctc + 2;

                    divideBySpaces = lineList.get(start).split("\\s");
                    for (y = 0; y < divideBySpaces.length; y++) {
                        if (divideBySpaces[y].contains("&&") || divideBySpaces[y].contains("||") || divideBySpaces[y].contains("&") || divideBySpaces[y].contains("|")) {
                            Ctc++;
                        }
                    }
                }

                if (lineList.get(start).contains("do") && lineList.get(start).contains("{")) {
                    System.out.println("Line number " + start + " has a do statement");
                    Ctc = Ctc + 2;

                    divideBySpaces = lineList.get(start).split("\\s");
                    for (y = 0; y < divideBySpaces.length; y++) {
                        if (divideBySpaces[y].contains("&&") || divideBySpaces[y].contains("||") || divideBySpaces[y].contains("&") || divideBySpaces[y].contains("|")) {
                            Ctc++;
                        }
                    }
                }

                if (lineList.get(start).contains("catch") && lineList.get(start).contains("(") && lineList.get(start).contains(")") && lineList.get(start).contains("{")) {
                    System.out.println("Line number " + start + " has a Catch statement");
                    Ctc++;

                }

                if (lineList.get(start).contains("switch") && lineList.get(start).contains("(") && lineList.get(start).contains(")") && lineList.get(start).contains("{")) {
                    System.out.println("Line number " + start + " has a Switch statement");

                    divideBySpaces = lineList.get(start).split("\\s");
                    for (y = 0; y < divideBySpaces.length; y++) {
                        if (divideBySpaces[y].contains("{")) {
                            bracketCheckSwitch.add("{");
                        }
                    }

                    for (z = start + 1; z < end; z++) {
                        lineToChars = lineList.get(z).split("(?!^)");
                        for (a = 0; a < lineToChars.length; a++) {

                            if (lineToChars[a].contains("{")) {
                                bracketCheckSwitch.add("{");
                                System.out.println("{");
                                System.out.println(bracketCheckSwitch.toString());
                            }
                            if (lineToChars[a].contains("}")) {
                                bracketCheckSwitch.remove(bracketCheckSwitch.size() - 1);
                                System.out.println("}");
                                System.out.println(bracketCheckSwitch.toString());
                            }

                        }
                        if (bracketCheckSwitch.toString() == "[]") {
                            break;
                        }
                    }
                    System.out.println("Switch is from " + start + " to " + z);
                    switchStart = start;
                    switchEnd = z;

                    while (switchStart < switchEnd + 1) {
                        if (lineList.get(switchStart).contains("case")) {
                            numberOfCases++;
                        }
                        switchStart++;
                    }

                    Ctc = Ctc + numberOfCases;
                }

                StatementLine s1 = new StatementLine(start, Ctc);
                StatementList.add(s1);
                start++;
            }
            System.out.println("");
        }

        ArrayList<StatementLine> displayCtcList = new ArrayList<>();

        for (y = 0; y < lineList.size(); y++) {
            isLinePresent = true;
            for (a = 0; a < StatementList.size(); a++) {
                if (StatementList.get(a).getLineNumber() == y) {
                    isLinePresent = false;
                }
            }
            if (isLinePresent) {
                StatementList.add(new StatementLine(y, 0));
            }
        }

        for (y = 1; y <= StatementList.size(); y++) {
            for (z = 0; z < StatementList.size(); z++) {
                if (StatementList.get(z).getLineNumber() + 1 == y) {
                    StatementLine s1 = new StatementLine(y, StatementList.get(z).getComplexity());
                    displayCtcList.add(s1);
                }
            }
        }

        return displayCtcList;

    }

    public static ArrayList<StatementLine> calculateComplexityByNestingControlStructure(ArrayList<Method> functionList) {

        int x, a, y, z;
        int start;
        int end;
        int Cnc;
        boolean isLinePresent;
        String[] lineToChars;
        ArrayList<String> bracketNesting = new ArrayList<>();

        ArrayList<StatementLine> StatementList = new ArrayList<>();
        for (x = 0; x < functionList.size(); x++) {

            Method f1 = functionList.get(x);
            start = f1.getStartLine();
            end = f1.getEndLine();

            while (start != end + 1) {
                Cnc = 0;

                if ((lineList.get(start).contains("if") && lineList.get(start).contains("(") && lineList.get(start).contains("(")) || (lineList.get(start).contains("for") && lineList.get(start).contains("(") && lineList.get(start).contains("(") && lineList.get(start).contains("{")) || (lineList.get(start).contains("while") && lineList.get(start).contains("(") && lineList.get(start).contains("(")) || (lineList.get(start).contains("for") && lineList.get(start).contains("(") && lineList.get(start).contains("(") && lineList.get(start).contains("{")) || (lineList.get(start).contains("do") && lineList.get(start).contains("{")) || (lineList.get(start).contains("switch") && lineList.get(start).contains("(") && lineList.get(start).contains("(") && lineList.get(start).contains("{"))) {
                    lineToChars = lineList.get(start).split("(?!^)");

                    for (a = 0; a < lineToChars.length; a++) {
//						 System.out.println(lineToChars[a]);
                        if (lineToChars[a].contains("{")) {
                            bracketNesting.add("{");
                            System.out.println(bracketNesting.get(bracketNesting.size() - 1));
                        }
                        if (lineToChars[a].contains("}")) {
                            bracketNesting.remove(bracketNesting.size() - 1);
                        }
                    }
                }

                lineToChars = lineList.get(start).split("(?!^)");

                for (a = 0; a < lineToChars.length; a++) {
//					 System.out.println(lineToChars[a]);
                    if (bracketNesting.toString() == "[]") {
                        break;
                    }
                    if (lineToChars[a].contains("}")) {
                        bracketNesting.remove(bracketNesting.size() - 1);
                    }
                }
                Cnc = Cnc + bracketNesting.size();
                //				 System.out.println("Cnc ------------------------------------------------------------------------------------");
                //				 System.out.println("Linu number" + (start+1) + "Cnc is" +Cnc);

                StatementLine s1 = new StatementLine(start, Cnc);
                StatementList.add(s1);
                start++;
            }

        }

        ArrayList<StatementLine> displayCtcList = new ArrayList<>();

        for (y = 0; y < lineList.size(); y++) {
            isLinePresent = true;
            for (a = 0; a < StatementList.size(); a++) {
                if (StatementList.get(a).getLineNumber() == y) {
                    isLinePresent = false;
                }
            }
            if (isLinePresent) {
                StatementList.add(new StatementLine(y, 0));
            }
        }

        for (y = 1; y <= StatementList.size(); y++) {
            for (z = 0; z < StatementList.size(); z++) {
                if (StatementList.get(z).getLineNumber() + 1 == y) {
                    StatementLine s1 = new StatementLine(y, StatementList.get(z).getComplexity());
                    displayCtcList.add(s1);
                }
            }
        }
        return displayCtcList;
    }

}
