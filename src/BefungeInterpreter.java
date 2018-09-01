import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Stack;
import java.util.Random;


public class BefungeInterpreter {

    private int direction =0;        // right=0, left=1, up=2, down=3
    private int[] currIndex = {0,0}; // x,y
    private char[][] codeArr = null; // codeArr[y][x]

    public String interpret(String code) {
        String[] lines = code.split("\n");
        codeArr = new char[lines.length][];
        for (int i=0; i<lines.length; i++)
            codeArr[i] = lines[i].toCharArray();

        Stack<Integer> stack = new Stack<Integer>();
        char c, v;
        int a,b;
        Random rand = new Random();
        boolean stringMode = false;
        String output="";
        do {
            c = codeArr[currIndex[1]][currIndex[0]];
            if (stringMode) {
                if (c == '\"') {
                    stringMode = false;
                } else {
                    stack.push((int)c);
                }
            } else {
                switch(c) {
                    case '+':
                        stack.push(stack.pop() + stack.pop()); break;
                    case '-':
                        a = stack.pop(); b = stack.pop();
                        stack.push(b - a);
                        break;
                    case '*':
                        stack.push(stack.pop() * stack.pop());
                        break;
                    case '/':
                        a = stack.pop(); b = stack.pop();
                        stack.push(a!= 0 ? b / a : 0);
                        break;
                    case '%':
                        a = stack.pop(); b = stack.pop();
                        stack.push(a!= 0 ? b % a : 0);
                        break;
                    case '!':
                        stack.push(stack.pop() == 0 ? 1 : 0);
                        break;
                    case '`':
                        stack.push(stack.pop() < stack.pop() ? 1 : 0);
                        break;
                    case '>':
                        direction = 0;
                        break;
                    case '<':
                        direction = 1;
                        break;
                    case 'v':
                        direction = 2;
                        break;
                    case '^':
                        direction = 3;
                        break;
                    case '?':
                        direction = rand.nextInt(4);
                        break;
                    case '_':
                        direction = stack.pop() == 0 ? 0 : 1;
                        break;
                    case '|':
                        direction = stack.pop() == 0 ? 2 : 3;
                        break;
                    case '\"':
                        stringMode = true;
                        break;
                    case ':':
                        stack.push(stack.empty() ? 0 : stack.peek());
                        break;
                    case '\\':
                        a = stack.pop(); b=stack.empty() ? 0 : stack.pop();
                        stack.push(a);
                        stack.push(b);
                        break;
                    case '$':
                        stack.pop();
                        break;
                    case '.':
                        output += Integer.toString(stack.pop());
                        break;
                    case ',':
                        output += (char) stack.pop().intValue();
                        break;
                    case '#':
                        nextIndex();
                        break;
                    case 'p':
                        b = stack.pop();
                        a = stack.pop();
                        v = (char) stack.pop().intValue();
                        codeArr[b][a] = v;
                        break;
                    case 'g':
                        b = stack.pop();
                        a = stack.pop();
                        stack.push((int)codeArr[b][a]);
                        break;
                    case ' ':
                        break;
                    default:
                        if (Character.isDigit(c)) {
                            stack.push(c-'0');
                        }
                        break;
                }
            }
            nextIndex();
        } while(c != '@');
        return output;
    }


    private void nextIndex() {
        switch (direction) {
            case 0: // right
                currIndex[0] = (currIndex[0]+1) % codeArr[currIndex[1]].length;
                break;
            case 1: // left
                currIndex[0] = (currIndex[0]-1 != -1) ? currIndex[0]-1 : codeArr[currIndex[1]].length-1;
                break;
            case 2: // down
                currIndex[1] = (currIndex[1]+1)%codeArr.length;
                break;
            case 3: // up
                currIndex[1] = (currIndex[1]-1 != -1) ? currIndex[1]-1 : codeArr.length-1;
                break;
        }
    }

    public static void main (String[] args) {
        if (args.length != 1) {
            System.out.println("Please include the name of the file");
        } else {
            try {
                ClassLoader classLoader = BefungeInterpreter.class.getClassLoader();
                URL pathUrl = classLoader.getResource("files/"+args[0]+".bf");
                String data = new String(Files.readAllBytes(Paths.get(pathUrl.toURI())));
                BefungeInterpreter befungeInterpreter = new BefungeInterpreter();
                System.out.print(befungeInterpreter.interpret(data));
            } catch (NullPointerException npe) {
                System.out.println("File does not exist");
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
