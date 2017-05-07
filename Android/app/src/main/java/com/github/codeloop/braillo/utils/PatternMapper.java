package com.github.codeloop.braillo.utils;

import java.util.Arrays;

/**
 * <p>
 * Created by Angad Singh on 6/5/17.
 * </p>
 */

public class PatternMapper {
    public static int A[][] = {
        {1,0},
        {0,0},
        {0,0}
    };

    public static int B[][] = {
        {1,0},
        {1,0},
        {0,0}
    };

    public static int C[][] = {
        {1,1},
        {0,0},
        {0,0}
    };

    public static int D[][] = {
        {1,1},
        {0,1},
        {0,0}
    };

    public static int E[][] = {
        {1,0},
        {0,1},
        {0,0}
    };

    public static int F[][] = {
        {1,1},
        {1,0},
        {0,0}
    };

    public static int G[][] = {
        {1,1},
        {1,1},
        {0,0}
    };

    public static int H[][] = {
        {1,0},
        {1,1},
        {0,0}
    };

    public static int I[][] = {
        {0,1},
        {1,0},
        {0,0}
    };

    public static int J[][] = {
        {0,1},
        {1,1},
        {0,0}
    };

    public static int K[][] = {
        {1,0},
        {0,0},
        {1,0}
    };

    public static int L[][] = {
        {1,0},
        {1,0},
        {1,0}
    };

    public static int M[][] = {
        {1,1},
        {0,0},
        {1,0}
    };

    public static int N[][] = {
        {1,1},
        {0,1},
        {1,0}
    };

    public static int O[][] = {
        {1,0},
        {0,1},
        {1,0}
    };

    public static int P[][] = {
        {1,1},
        {1,0},
        {1,0}
    };

    public static int Q[][] = {
        {1,1},
        {1,1},
        {1,0}
    };

    public static int R[][] = {
        {1,0},
        {1,1},
        {1,0}
    };

    public static int S[][] = {
        {0,1},
        {1,0},
        {1,0}
    };

    public static int T[][] = {
        {0,1},
        {1,1},
        {1,0}
    };

    public static int U[][] = {
        {1,0},
        {0,0},
        {1,1}
    };

    public static int V[][] = {
        {1,0},
        {1,0},
        {1,1}
    };

    public static int W[][] = {
        {0,1},
        {1,1},
        {0,1}
    };

    public static int X[][] = {
        {1,1},
        {0,0},
        {1,1}
    };

    public static int Y[][] = {
        {1,1},
        {0,1},
        {1,1}
    };

    public static int Z[][] = {
        {1,0},
        {0,1},
        {1,1}
    };

    public static int SPACE[][] = {
            {0,0},
            {0,0},
            {0,0}
    };

    public static String compare(int[][] x) {
        if(Arrays.deepEquals(A,x))
            return "a";
        else if(Arrays.deepEquals(B,x))
            return "b";
        else if(Arrays.deepEquals(C,x))
            return "c";
        else if(Arrays.deepEquals(D,x))
            return "d";
        else if(Arrays.deepEquals(E,x))
            return "e";
        else if(Arrays.deepEquals(F,x))
            return "f";
        else if(Arrays.deepEquals(G,x))
            return "g";
        else if(Arrays.deepEquals(H,x))
            return "h";
        else if(Arrays.deepEquals(I,x))
            return "i";
        else if(Arrays.deepEquals(J,x))
            return "j";
        else if(Arrays.deepEquals(K,x))
            return "k";
        else if(Arrays.deepEquals(L,x))
            return "l";
        else if(Arrays.deepEquals(M,x))
            return "m";
        else if(Arrays.deepEquals(N,x))
            return "n";
        else if(Arrays.deepEquals(O,x))
            return "o";
        else if(Arrays.deepEquals(P,x))
            return "p";
        else if(Arrays.deepEquals(Q,x))
            return "q";
        else if(Arrays.deepEquals(R,x))
            return "r";
        else if(Arrays.deepEquals(S,x))
            return "s";
        else if(Arrays.deepEquals(T,x))
            return "t";
        else if(Arrays.deepEquals(U,x))
            return "u";
        else if(Arrays.deepEquals(V,x))
            return "v";
        else if(Arrays.deepEquals(W,x))
            return "w";
        else if(Arrays.deepEquals(X,x))
            return "x";
        else if(Arrays.deepEquals(Y,x))
            return "y";
        else if(Arrays.deepEquals(Z,x))
            return "z";
        else if(Arrays.deepEquals(SPACE,x))
            return " ";
        else
            return "";
    }


    public static int[][] getArray(String str) {
        switch (str) {
            case "a":return A;
            case "b":return B;
            case "c":return C;
            case "d":return D;
            case "e":return E;
            case "f":return F;
            case "g":return G;
            case "h":return H;
            case "i":return I;
            case "j":return J;
            case "k":return K;
            case "l":return L;
            case "m":return M;
            case "n":return N;
            case "o":return O;
            case "p":return P;
            case "q":return Q;
            case "r":return R;
            case "s":return S;
            case "t":return T;
            case "u":return U;
            case "v":return V;
            case "w":return W;
            case "x":return X;
            case "y":return Y;
            case "z":return Z;
        }
        return A;
    }

}
