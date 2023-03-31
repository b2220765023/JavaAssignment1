
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader moves = null;
        BufferedReader readBoard;
        BufferedWriter bw;
        try {
            readBoard = new BufferedReader(new FileReader("board.txt"));    // reading board
            bw = new BufferedWriter(new FileWriter("output.txt"));          // writing to a .txt file
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //Writing board
        boolean over = false;
        List<String> boardList = new ArrayList<String>();                   // all board will be kept in this ArrayList
        String line;
        int[] indexLineOfWhiteBall = new int[1];
        String whiteBall = "*";

        bw.write("Game board:");
        bw.newLine();
        while ((line = readBoard.readLine()) != null) {                     // writing the board
            boardList.add(line);
            bw.write(line);
            if (line.contains(whiteBall)) {
                indexLineOfWhiteBall[0] = boardList.indexOf(line);
            }
            bw.newLine();
        }
        bw.newLine();
        moves = new BufferedReader(new FileReader("move.txt"));
        String outputMovesList = moves.readLine();
        String movesList = outputMovesList.replaceAll("\\s", "");

        int score = 0;
        String currMove;
        int whiteBallIndex = indexLineOfWhiteBall[0];
        List<String> playedMoves = new ArrayList<String>();             // list of played moves, (in case ball went to hole without moves finished.

        for (int i = 0; i < movesList.length(); i++) {                  // main loop which contains every move nad every board changes.
            String lineOfWhiteBall = boardList.get(whiteBallIndex);
            int positionOfWhiteBall = boardList.get(whiteBallIndex).indexOf(whiteBall); // position of the ball at the white ball's index
            currMove = String.valueOf(movesList.charAt(i));

            if (currMove.equals("L")) {      //Controls Current Move.                                           //I Will explain this only this "if" block because rest will be the same.
                if (positionOfWhiteBall != 0) {
                    if (String.valueOf(lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) - 2)).equals("B")) {
                        String newChar = "X";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall - 2) + whiteBall + " " + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score -= 5;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) - 2)).equals("H")) {
                        String newChar = "H";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall - 2) + newChar + "  " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        over = true;            // for writing "Game Over!" at the line 684.
                        playedMoves.add("L");
                        break;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) - 2)).equals("W")) {  // for classic W situation like "W * A B C"
                        if (positionOfWhiteBall == boardList.get(whiteBallIndex).length() - 1) {                                            // Output --> "W * A B C "
                            char oldChar = lineOfWhiteBall.charAt(0);
                            score += pointsOfSpecials(String.valueOf(oldChar));
                            String newChar = isSpecial(String.valueOf(oldChar));
                            String newLine = whiteBall + lineOfWhiteBall.substring(1, positionOfWhiteBall) + newChar;
                            lineOfWhiteBall = newLine;
                            if (newChar.equals("H")) {                                                                  // for position like "W * H A B"
                                newLine = "H" + lineOfWhiteBall.substring(1, positionOfWhiteBall) + " ";                // Output --> "W  H A B"     White Ball became blank(white space).
                                boardList.remove(whiteBallIndex);
                                boardList.add(whiteBallIndex, newLine);
                                over = true;
                                playedMoves.add("L");
                                break;
                            }
                            lineOfWhiteBall = newLine;
                            boardList.remove(whiteBallIndex);
                            boardList.add(whiteBallIndex, newLine);
                        } else {        // at the other else parts, I calculated special characters(W,B,R,Y). At this else part calculated the other balls(G,P,O,L,N...)
                            char oldChar = lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) + 2);
                            score += pointsOfSpecials(String.valueOf(oldChar));
                            String newChar = isSpecial(String.valueOf(oldChar));
                            String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + " " + whiteBall + lineOfWhiteBall.substring(positionOfWhiteBall + 3);
                            lineOfWhiteBall = newLine;
                            if (newChar.equals("H")) {
                                newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + " H " + lineOfWhiteBall.substring(positionOfWhiteBall + 4);
                                boardList.remove(whiteBallIndex);
                                boardList.add(whiteBallIndex, newLine);
                                over = true;
                                playedMoves.add("L");
                                break;
                            }
                            lineOfWhiteBall = newLine;
                            boardList.remove(whiteBallIndex);
                            boardList.add(whiteBallIndex, newLine);
                        }
                    } else if (String.valueOf(lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) - 2)).equals("Y")) {
                        String newChar = "X";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall - 2) + whiteBall + " " + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score += 5;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) - 2)).equals("R")) {
                        String newChar = "X";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall - 2) + whiteBall + " " + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score += 10;
                    } else {
                        char newChar = lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) - 2);
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall - 2) + whiteBall + " " + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                    }
                }//*****************************************************************************************************
                else if (positionOfWhiteBall == 0) {        // if the white ball's position at the start of the lineOfWhiteBall.

                    if (String.valueOf(lineOfWhiteBall.charAt(lineOfWhiteBall.length() - 1)).equals("B")) {
                        String newChar = "X";
                        String newLine = newChar + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 2, lineOfWhiteBall.length() - 1) + whiteBall;
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score -= 5;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(lineOfWhiteBall.length() - 1)).equals("H")) {
                        String newChar = "H";
                        String newLine = " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        over = true;
                        playedMoves.add("L");
                        break;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(lineOfWhiteBall.length() - 1)).equals("Y")) {
                        String newChar = "X";
                        String newLine = newChar + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 2, lineOfWhiteBall.length() - 1) + whiteBall;
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score += 5;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(lineOfWhiteBall.length() - 1)).equals("R")) {
                        String newChar = "X";
                        String newLine = newChar + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 2, lineOfWhiteBall.length() - 1) + whiteBall;
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score += 10;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(lineOfWhiteBall.length() - 1)).equals("W")) { // for position like "* A B C D W"   and move to left.
                        char oldChar = lineOfWhiteBall.charAt(positionOfWhiteBall + 2);                            // output will be like "A * B C D W".
                        score += pointsOfSpecials(String.valueOf(oldChar));
                        String newChar = isSpecial(String.valueOf(oldChar));
                        String newLine = newChar + " " + whiteBall + lineOfWhiteBall.substring(positionOfWhiteBall + 3);
                        lineOfWhiteBall = newLine;
                        if (newChar.equals("H")) {                                                                 //for position like "* H B C D W.
                            newLine = "  H" + lineOfWhiteBall.substring(positionOfWhiteBall + 3);
                            boardList.remove(whiteBallIndex);
                            boardList.add(whiteBallIndex, newLine);
                            over = true;
                            playedMoves.add("L");
                            break;
                        }
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                    } else {
                        char newChar = lineOfWhiteBall.charAt(lineOfWhiteBall.length() - 1);
                        String newLine = newChar + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 2, lineOfWhiteBall.length() - 1) + whiteBall;
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                    }
                }
                playedMoves.add("L");
                //******************************************************************************************************
            } else if (currMove.equals("R")) {
                if (positionOfWhiteBall != lineOfWhiteBall.length() - 1) {
                    if (String.valueOf(lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) + 2)).equals("B")) {
                        String newChar = "X";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + " " + whiteBall + lineOfWhiteBall.substring(positionOfWhiteBall + 3);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score -= 5;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) + 2)).equals("H")) {
                        String newChar = "H";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        over = true;
                        playedMoves.add("R");
                        break;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) + 2)).equals("Y")) {
                        String newChar = "X";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + " " + whiteBall + lineOfWhiteBall.substring(positionOfWhiteBall + 3);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score += 5;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) + 2)).equals("R")) {
                        String newChar = "X";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + " " + whiteBall + lineOfWhiteBall.substring(positionOfWhiteBall + 3);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score += 10;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(boardList.get(whiteBallIndex).indexOf(whiteBall) + 2)).equals("W")) {
                        if (positionOfWhiteBall == 0) {
                            char oldChar = lineOfWhiteBall.charAt(lineOfWhiteBall.length() - 1);
                            score += pointsOfSpecials(String.valueOf(oldChar));
                            String newChar = isSpecial(String.valueOf(oldChar));
                            String newLine = newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1, lineOfWhiteBall.length() - 1) + whiteBall;
                            lineOfWhiteBall = newLine;
                            if (newChar.equals("H")) {
                                newLine = "  " + lineOfWhiteBall.substring(2, positionOfWhiteBall + 8) + "H";
                                boardList.remove(whiteBallIndex);
                                boardList.add(whiteBallIndex, newLine);
                                over = true;
                                playedMoves.add("R");
                                break;
                            }
                            lineOfWhiteBall = newLine;
                            boardList.remove(whiteBallIndex);
                            boardList.add(whiteBallIndex, newLine);
                        } else {
                            char oldChar = lineOfWhiteBall.charAt(positionOfWhiteBall - 2);
                            score += pointsOfSpecials(String.valueOf(oldChar));
                            String newChar = isSpecial(String.valueOf(oldChar));
                            String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall - 2) + whiteBall + " " + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                            lineOfWhiteBall = newLine;
                            if (newChar.equals("H")) {
                                newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall - 2) + "H  " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                                boardList.remove(whiteBallIndex);
                                boardList.add(whiteBallIndex, newLine);
                                over = true;
                                playedMoves.add("R");
                                break;
                            }
                            lineOfWhiteBall = newLine;
                            boardList.remove(whiteBallIndex);
                            boardList.add(whiteBallIndex, newLine);
                        }
                    } else {
                        char newChar = lineOfWhiteBall.charAt(positionOfWhiteBall + 2);
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + " " + whiteBall + lineOfWhiteBall.substring(positionOfWhiteBall + 3);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                    }
                    //******************************************************************************************************
                } else if (positionOfWhiteBall == lineOfWhiteBall.length() - 1) {         //if ball is at the end of the board and move is right situation
                    if (String.valueOf(lineOfWhiteBall.charAt(0)).equals("B")) {
                        String newChar = "X";
                        String newLine = whiteBall + " " + lineOfWhiteBall.substring(2, positionOfWhiteBall) + newChar;
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score -= 5;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(0)).equals("H")) {
                        String newChar = "H";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        over = true;
                        playedMoves.add("R");
                        break;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(0)).equals("Y")) {
                        String newChar = "X";
                        String newLine = whiteBall + " " + lineOfWhiteBall.substring(2, positionOfWhiteBall) + newChar;
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score += 5;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(0)).equals("R")) {
                        String newChar = "X";
                        String newLine = whiteBall + " " + lineOfWhiteBall.substring(2, positionOfWhiteBall) + newChar;
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        score += 10;
                    } else if (String.valueOf(lineOfWhiteBall.charAt(0)).equals("W")) {
                        char oldChar = lineOfWhiteBall.charAt(positionOfWhiteBall - 2);
                        score += pointsOfSpecials(String.valueOf(oldChar));
                        String newChar = isSpecial(String.valueOf(oldChar));
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall - 2) + whiteBall + " " + newChar;
                        lineOfWhiteBall = newLine;
                        if (newChar.equals("H")) {
                            newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall - 2) + "H " + " ";
                            boardList.remove(whiteBallIndex);
                            boardList.add(whiteBallIndex, newLine);
                            over = true;
                            playedMoves.add("R");
                            break;
                        }
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                    } else {
                        char newChar = lineOfWhiteBall.charAt(0);
                        String newLine = whiteBall + " " + lineOfWhiteBall.substring(2, positionOfWhiteBall) + newChar;
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                    }
                }
                playedMoves.add("R");
                //*****************************************************************************************************
            } else if (currMove.equals("U")) {
                if (whiteBallIndex != 0) {
                    String upLine = boardList.get(whiteBallIndex - 1);
                    if (String.valueOf(boardList.get(whiteBallIndex - 1).charAt(positionOfWhiteBall)).equals("B")) {
                        String newChar = "X";
                        String newLine1 = upLine.substring(0, positionOfWhiteBall - 1) + " " + whiteBall + upLine.substring(positionOfWhiteBall + 1);
                        String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        upLine = newLine1;
                        lineOfWhiteBall = newLine2;
                        boardList.remove(whiteBallIndex - 1);
                        boardList.remove(whiteBallIndex - 1);
                        boardList.add(whiteBallIndex - 1, newLine1);
                        boardList.add(whiteBallIndex, newLine2);
                        score -= 5;
                    } else if (String.valueOf(boardList.get(whiteBallIndex - 1).charAt(positionOfWhiteBall)).equals("H")) {
                        String newChar = "H";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        over = true;
                        playedMoves.add("U");
                        break;
                    } else if (String.valueOf(boardList.get(whiteBallIndex - 1).charAt(positionOfWhiteBall)).equals("Y")) {
                        String newChar = "X";
                        String newLine1 = upLine.substring(0, positionOfWhiteBall - 1) + " " + whiteBall + upLine.substring(positionOfWhiteBall + 1);
                        String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        upLine = newLine1;
                        lineOfWhiteBall = newLine2;
                        boardList.remove(whiteBallIndex - 1);
                        boardList.remove(whiteBallIndex - 1);
                        boardList.add(whiteBallIndex - 1, newLine1);
                        boardList.add(whiteBallIndex, newLine2);
                        score += 5;
                    } else if (String.valueOf(boardList.get(whiteBallIndex - 1).charAt(positionOfWhiteBall)).equals("R")) {
                        String newChar = "X";
                        String newLine1 = upLine.substring(0, positionOfWhiteBall - 1) + " " + whiteBall + upLine.substring(positionOfWhiteBall + 1);
                        String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        upLine = newLine1;
                        lineOfWhiteBall = newLine2;
                        boardList.remove(whiteBallIndex - 1);
                        boardList.remove(whiteBallIndex - 1);
                        boardList.add(whiteBallIndex - 1, newLine1);
                        boardList.add(whiteBallIndex, newLine2);
                        score += 10;
                    } else if (String.valueOf(boardList.get(whiteBallIndex - 1).charAt(positionOfWhiteBall)).equals("W")) {
                        if (whiteBallIndex != boardList.size() - 1) {
                            char oldChar = boardList.get(whiteBallIndex + 1).charAt(positionOfWhiteBall);
                            score += pointsOfSpecials(String.valueOf(oldChar));
                            String newChar = isSpecial(String.valueOf(oldChar));
                            String newLine1 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                            String newLine2 = boardList.get(whiteBallIndex + 1).substring(0, positionOfWhiteBall) + whiteBall + boardList.get(whiteBallIndex + 1).substring(positionOfWhiteBall + 1);
                            upLine = newLine1;
                            if (newChar.equals("H")) {
                                newLine1 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                                boardList.remove(whiteBallIndex);
                                boardList.add(whiteBallIndex, newLine1);
                                over = true;
                                playedMoves.add("U");
                                break;
                            }
                            boardList.remove(whiteBallIndex);
                            boardList.remove(whiteBallIndex);
                            boardList.add(whiteBallIndex, newLine1);
                            boardList.add(whiteBallIndex + 1, newLine2);
                            whiteBallIndex += 2;
                        } else {
                            char oldChar = boardList.get(0).charAt(positionOfWhiteBall);
                            score += pointsOfSpecials(String.valueOf(oldChar));
                            String newChar = isSpecial(String.valueOf(oldChar));
                            String newLine1 = boardList.get(0).substring(0, positionOfWhiteBall) + whiteBall + boardList.get(0).substring(positionOfWhiteBall + 1);
                            String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                            if (newChar.equals("H")) {
                                newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                                boardList.remove(whiteBallIndex);
                                boardList.add(whiteBallIndex, newLine2);
                                over = true;
                                playedMoves.add("U");
                                break;
                            }
                            boardList.remove(0);
                            boardList.add(0, newLine1);
                            boardList.remove(boardList.size() - 1);
                            boardList.add(boardList.size(), newLine2);
                            whiteBallIndex = 0;
                        }
                    } else {
                        char newChar = boardList.get(whiteBallIndex - 1).charAt(positionOfWhiteBall);
                        String newLine1 = upLine.substring(0, positionOfWhiteBall - 1) + " " + whiteBall + upLine.substring(positionOfWhiteBall + 1);
                        String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        upLine = newLine1;
                        lineOfWhiteBall = newLine2;
                        boardList.remove(whiteBallIndex - 1);
                        boardList.remove(whiteBallIndex - 1);
                        boardList.add(whiteBallIndex - 1, newLine1);
                        boardList.add(whiteBallIndex, newLine2);
                    }
                    whiteBallIndex--;
                } else if (whiteBallIndex == 0) {
                    if (String.valueOf(boardList.get(boardList.size() - 1).charAt(positionOfWhiteBall)).equals("W")) {
                        char oldChar = boardList.get(whiteBallIndex + 1).charAt(positionOfWhiteBall);
                        score += pointsOfSpecials(String.valueOf(oldChar));
                        String newChar = isSpecial(String.valueOf(oldChar));
                        String newLine1 = boardList.get(0).substring(0, positionOfWhiteBall) + newChar + boardList.get(0).substring(positionOfWhiteBall + 1);
                        String newLine2 = boardList.get(1).substring(0, positionOfWhiteBall) + whiteBall + boardList.get(1).substring(positionOfWhiteBall + 1);
                        if (newChar.equals("H")) {
                            newLine1 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                            boardList.remove(whiteBallIndex);
                            boardList.add(whiteBallIndex, newLine1);
                            over = true;
                            playedMoves.add("U");
                            break;
                        }
                        boardList.remove(0);
                        boardList.add(0, newLine1);
                        boardList.remove(1);
                        boardList.add(1, newLine2);
                        whiteBallIndex = 1;
                    } else if (String.valueOf(boardList.get(boardList.size() - 1).charAt(positionOfWhiteBall)).equals("H")) {
                        String newChar = "H";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        over = true;
                        playedMoves.add("U");
                        break;
                    } else if (String.valueOf(boardList.get(boardList.size() - 1).charAt(positionOfWhiteBall)).equals("B")) {
                        String bottomLine = boardList.get(boardList.size() - 1);
                        String newChar = "X";
                        String newLine1 = boardList.get(0).substring(0, positionOfWhiteBall) + newChar + boardList.get(0).substring(positionOfWhiteBall + 1);
                        String newLine2 = boardList.get(boardList.size() - 1).substring(0, positionOfWhiteBall) + whiteBall + boardList.get(boardList.size() - 1).substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine2;
                        boardList.remove(boardList.size() - 1);
                        boardList.add(boardList.size(), newLine2);
                        boardList.remove(0);
                        boardList.add(0, newLine1);
                        whiteBallIndex = boardList.size() - 1;
                        score -= 5;
                    } else if (String.valueOf(boardList.get(boardList.size() - 1).charAt(positionOfWhiteBall)).equals("Y")) {
                        String bottomLine = boardList.get(boardList.size() - 1);
                        String newChar = "X";
                        String newLine1 = boardList.get(0).substring(0, positionOfWhiteBall) + newChar + boardList.get(0).substring(positionOfWhiteBall + 1);
                        String newLine2 = boardList.get(boardList.size() - 1).substring(0, positionOfWhiteBall) + whiteBall + boardList.get(boardList.size() - 1).substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine2;
                        boardList.remove(boardList.size() - 1);
                        boardList.add(boardList.size(), newLine2);
                        boardList.remove(0);
                        boardList.add(0, newLine1);
                        whiteBallIndex = boardList.size() - 1;
                        score += 5;
                    } else if (String.valueOf(boardList.get(boardList.size() - 1).charAt(positionOfWhiteBall)).equals("R")) {
                        String bottomLine = boardList.get(boardList.size() - 1);
                        String newChar = "X";
                        String newLine1 = boardList.get(0).substring(0, positionOfWhiteBall) + newChar + boardList.get(0).substring(positionOfWhiteBall + 1);
                        String newLine2 = boardList.get(boardList.size() - 1).substring(0, positionOfWhiteBall) + whiteBall + boardList.get(boardList.size() - 1).substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine2;
                        boardList.remove(boardList.size() - 1);
                        boardList.add(boardList.size(), newLine2);
                        boardList.remove(0);
                        boardList.add(0, newLine1);
                        whiteBallIndex = boardList.size() - 1;
                        score += 10;
                    } else {
                        String bottomLine = boardList.get(boardList.size() - 1);
                        char newChar = boardList.get(boardList.size() - 1).charAt(positionOfWhiteBall);
                        String newLine1 = boardList.get(0).substring(0, positionOfWhiteBall) + newChar + boardList.get(0).substring(positionOfWhiteBall + 1);
                        String newLine2 = boardList.get(boardList.size() - 1).substring(0, positionOfWhiteBall) + whiteBall + boardList.get(boardList.size() - 1).substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine2;
                        boardList.remove(boardList.size() - 1);
                        boardList.add(boardList.size(), newLine2);
                        boardList.remove(0);
                        boardList.add(0, newLine1);
                        whiteBallIndex = boardList.size() - 1;
                    }
                }
                playedMoves.add("U");
                //**********************************************************************************************************
            } else if (currMove.equals("D")) {
                if (whiteBallIndex != boardList.size() - 1) {
                    String downLine = boardList.get(whiteBallIndex + 1);
                    if (String.valueOf(boardList.get(whiteBallIndex + 1).charAt(positionOfWhiteBall)).equals("B")) {
                        String newChar = "X";
                        String newLine1 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        String newLine2 = downLine.substring(0, positionOfWhiteBall) + whiteBall + downLine.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine2;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine1);
                        boardList.remove(whiteBallIndex + 1);
                        boardList.add(whiteBallIndex + 1, newLine2);
                        score -= 5;
                    } else if (String.valueOf(boardList.get(whiteBallIndex + 1).charAt(positionOfWhiteBall)).equals("H")) {
                        String newChar = "H";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        over = true;
                        playedMoves.add("D");
                        break;
                    } else if (String.valueOf(boardList.get(whiteBallIndex + 1).charAt(positionOfWhiteBall)).equals("R")) {
                        String newChar = "X";
                        String newLine1 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        String newLine2 = downLine.substring(0, positionOfWhiteBall) + whiteBall + downLine.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine2;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine1);
                        boardList.remove(whiteBallIndex + 1);
                        boardList.add(whiteBallIndex + 1, newLine2);
                        score += 10;
                    } else if (String.valueOf(boardList.get(whiteBallIndex + 1).charAt(positionOfWhiteBall)).equals("Y")) {
                        String newChar = "X";
                        String newLine1 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        String newLine2 = downLine.substring(0, positionOfWhiteBall) + whiteBall + downLine.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine2;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine1);
                        boardList.remove(whiteBallIndex + 1);
                        boardList.add(whiteBallIndex + 1, newLine2);
                        score += 5;
                    } else if (String.valueOf(boardList.get(whiteBallIndex + 1).charAt(positionOfWhiteBall)).equals("W")) {
                        if (whiteBallIndex != 0) {
                            char oldChar = boardList.get(whiteBallIndex - 1).charAt(positionOfWhiteBall);
                            score += pointsOfSpecials(String.valueOf(oldChar));
                            String newChar = isSpecial(String.valueOf(oldChar));
                            String newLine1 = boardList.get(whiteBallIndex - 1).substring(0, positionOfWhiteBall) + whiteBall + boardList.get(whiteBallIndex - 1).substring(positionOfWhiteBall + 1);
                            String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + boardList.get(whiteBallIndex).substring(positionOfWhiteBall + 1);
                            if (newChar.equals("H")) {
                                newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                                boardList.remove(whiteBallIndex);
                                boardList.add(whiteBallIndex, newLine2);
                                over = true;
                                playedMoves.add("D");
                                break;
                            }
                            boardList.remove(whiteBallIndex - 1);
                            boardList.add(whiteBallIndex - 1, newLine1);
                            boardList.remove(whiteBallIndex);
                            boardList.add(whiteBallIndex, newLine2);
                            whiteBallIndex -= 2;
                        } else {
                            char oldChar = boardList.get(boardList.size() - 1).charAt(positionOfWhiteBall);
                            score += pointsOfSpecials(String.valueOf(oldChar));
                            String newChar = isSpecial(String.valueOf(oldChar));
                            String newLine1 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                            String newLine2 = boardList.get(boardList.size() - 1).substring(0, positionOfWhiteBall) + whiteBall + boardList.get(boardList.size() - 1).substring(positionOfWhiteBall + 1);
                            if (newChar.equals("H")) {
                                newLine1 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                                boardList.remove(whiteBallIndex);
                                boardList.add(whiteBallIndex, newLine1);
                                over = true;
                                playedMoves.add("D");
                                break;
                            }
                            boardList.remove(0);
                            boardList.add(0, newLine1);
                            boardList.remove(boardList.size() - 1);
                            boardList.add(boardList.size(), newLine2);
                            whiteBallIndex = boardList.size() - 2;
                        }
                    } else {
                        char newChar = boardList.get(whiteBallIndex + 1).charAt(positionOfWhiteBall);
                        String newLine1 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        String newLine2 = downLine.substring(0, positionOfWhiteBall) + whiteBall + downLine.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine2;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine1);
                        boardList.remove(whiteBallIndex + 1);
                        boardList.add(whiteBallIndex + 1, newLine2);
                    }
                    whiteBallIndex++;
                } else {
                    String upperLine = boardList.get(whiteBallIndex - 1);
                    String topLine = boardList.get(0);
                    if (String.valueOf(boardList.get(0).charAt(positionOfWhiteBall)).equals("B")) {
                        String newChar = "X";
                        String newLine1 = topLine.substring(0, positionOfWhiteBall) + whiteBall + topLine.substring(positionOfWhiteBall + 1);
                        String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine1;
                        boardList.remove(whiteBallIndex);
                        boardList.add(boardList.size(), newLine2);
                        boardList.remove(0);
                        boardList.add(0, newLine1);
                        score -= 5;
                        whiteBallIndex = 0;
                    } else if (String.valueOf(boardList.get(0).charAt(positionOfWhiteBall)).equals("H")) {
                        String newChar = "H";
                        String newLine = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine;
                        boardList.remove(whiteBallIndex);
                        boardList.add(whiteBallIndex, newLine);
                        over = true;
                        playedMoves.add("D");
                        break;
                    } else if (String.valueOf(boardList.get(0).charAt(positionOfWhiteBall)).equals("R")) {
                        String newChar = "X";
                        String newLine1 = topLine.substring(0, positionOfWhiteBall) + whiteBall + topLine.substring(positionOfWhiteBall + 1);
                        String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine1;
                        boardList.remove(whiteBallIndex);
                        boardList.add(boardList.size(), newLine2);
                        boardList.remove(0);
                        boardList.add(0, newLine1);
                        score += 10;
                        whiteBallIndex = 0;
                    } else if (String.valueOf(boardList.get(0).charAt(positionOfWhiteBall)).equals("Y")) {
                        String newChar = "X";
                        String newLine1 = topLine.substring(0, positionOfWhiteBall) + whiteBall + topLine.substring(positionOfWhiteBall + 1);
                        String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine1;
                        boardList.remove(whiteBallIndex);
                        boardList.add(boardList.size(), newLine2);
                        boardList.remove(0);
                        boardList.add(0, newLine1);
                        score += 5;
                        whiteBallIndex = 0;
                    } else if (String.valueOf(boardList.get(0).charAt(positionOfWhiteBall)).equals("W")) {
                        char oldChar = boardList.get(whiteBallIndex - 1).charAt(positionOfWhiteBall);
                        score += pointsOfSpecials(String.valueOf(oldChar));
                        String newChar = isSpecial(String.valueOf(oldChar));
                        String newLine1 = upperLine.substring(0, positionOfWhiteBall) + whiteBall + upperLine.substring(positionOfWhiteBall + 1);
                        String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        if (newChar.equals("H")) {
                            newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + " " + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                            boardList.remove(whiteBallIndex);
                            boardList.add(whiteBallIndex, newLine2);
                            over = true;
                            playedMoves.add("D");
                            break;
                        }
                        boardList.remove(whiteBallIndex - 1);
                        boardList.add(whiteBallIndex - 1, newLine1);
                        boardList.remove(boardList.size() - 1);
                        boardList.add(boardList.size(), newLine2);
                        whiteBallIndex = boardList.size() - 2;
                    } else {
                        String newChar = String.valueOf(boardList.get(0).charAt(positionOfWhiteBall));
                        String newLine1 = topLine.substring(0, positionOfWhiteBall) + whiteBall + topLine.substring(positionOfWhiteBall + 1);
                        String newLine2 = lineOfWhiteBall.substring(0, positionOfWhiteBall) + newChar + lineOfWhiteBall.substring(positionOfWhiteBall + 1);
                        lineOfWhiteBall = newLine1;
                        boardList.remove(whiteBallIndex);
                        boardList.add(boardList.size(), newLine2);
                        boardList.remove(0);
                        boardList.add(0, newLine1);
                        whiteBallIndex = 0;
                    }
                }
                playedMoves.add("D");
            }
        }

        bw.write("Your movement is:");
        bw.newLine();
        for (int i = 0; i < playedMoves.size(); i++) {      // block after the "Your movement is:" text.
            bw.write(playedMoves.get(i) + " ");             // writes the moves with print, not println for not writing under.
        }
        bw.newLine();
        bw.newLine();
        bw.write("Your output is:");
        bw.newLine();
        for (int i = 0; i < boardList.size(); i++) {
            bw.write(boardList.get(i));                     // writing the last appereance of the board.
            bw.newLine();
        }
        bw.newLine();
        if (over == true) {                                 // if white ball went to hole, this occurs.
            bw.write("Game Over!");
            bw.newLine();
        }
        bw.write("Score: " + score);
        bw.close();
    }
    private static String isSpecial(String newChar) {       // when this method called, it puts our character instead of old char , only if char is one of "B Y R" Chars.
        List<String> specialBalls = new ArrayList<String>();
        specialBalls.add("B");
        specialBalls.add("Y");
        specialBalls.add("R");
        if (specialBalls.contains(newChar)){
            newChar = "X";
        }
        return newChar;

    }private static int pointsOfSpecials(String newChar){   // this method checks the character after collising with wall. For example line is "W * R L G"
        int points = 0;                                     // after white ball collises with wall output was like "W X * L G" but we couldn't earn any points.
        if (newChar.equals("B")){                           // this method for fixing the problem
            points -= 5;
        }else if (newChar.equals("R")){
            points += 10;
        } else if (newChar.equals("Y")) {
            points += 5;
        }
        return points;
    }
}