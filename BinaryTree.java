package csproblem;

class node{
    public double w = 0;
    public char ch = '@';
    node Lson = null;
    node Rson = null;
    node(){w = 0; ch = '@'; }
    node(double b, char c){w = b; ch = c; }
}

public class BinaryTree{
    public node rt;
    public int cnt = 0;
    public String source = "";    //源字符串
    public double prefixAns = 0;    //前缀式求值结果
    public double infixAns = 0;    //中缀式求值结果
    public double suffixAns = 0;    //后缀式求值结果
    public String prefixStr = "";    //前缀表达式
    public String infixStr = "";    //中缀表达式
    public String suffixStr = "";    //后缀表达式
    public String generalizedTable = "";    //广义表
    public String expAnswer[] = new String[100];    //表达式结果数组，用来暂存
    public String opt = "+-*/()";    //操作符数组
    public String cmpr[]={    //操作符比较数组
        ">><<<>",
        ">><<<>",
        ">>>><>",
        ">>>><>",
        "<<<<<+",
        ">>>>$>"
    };
    //判断是不是运算符
    private boolean isOpt(char ch){
        for(int i = 0; i < opt.length(); i++){
            if(ch == opt.charAt(i))
                return true;
        }
        return false;
    }
    //完成一个运算
    private double calc(double num1, char opt, double num2){
        double ans = 0;
        if(opt == '+') ans = num1+num2;
        if(opt == '-') ans = num1-num2;
        if(opt == '*') ans = num1*num2;
        if(opt == '/') ans = num1/num2;
        return ans;
    }
    //得到运算符的下标
    private int getIdx(char ch){
        for(int i = 0; i < opt.length(); i++){
            if(opt.charAt(i) == ch)
                return i;
        }
        return -1;                                                                                                                                                                                                                                                                                                                                                   
    }
    //判断表达式是否合法
    public void isOkExp(){
    	boolean flag = true;
    	if(flag){
    		for(int i = 0; i < source.length(); i++){
    			char ch = source.charAt(i);
    			if(!(ch == '.' || isOpt(ch) || (ch>='0' && ch <= '9'))) flag = false;
    		}
    	}
    	if(flag){
    		char ch1 = source.charAt(0);
    		char ch2 = source.charAt(source.length()-1);
    		if(!(ch1>='0' && ch1<='9' || ch1=='(')) flag = false;
    		if(!(ch2>='0' && ch2<='9' || ch2==')')) flag = false;
    	} 
    	if(flag){
            char st[] = new char[1000]; int top = 0;
            for(int i = 0; i < source.length(); i++){
                if(!flag) break;
                if(source.charAt(i) == '('){
                    st[top++] = source.charAt(i);
                }
                if(source.charAt(i) == ')'){
                    if(top == 0) flag = false;
                    else if(st[top-1] != '(') flag = false;
                    else top--;
                }
            }
    	}
    	if(flag){
    		for(int i = 1; i < source.length(); i++){
    			if(isOpt(source.charAt(i)) && isOpt(source.charAt(i-1))){
    				if(getIdx(source.charAt(i-1)) <= 3 && source.charAt(i) == ')') flag = false;
    				if(source.charAt(i-1) == '(' && getIdx(source.charAt(i)) <= 3) flag = false;
    				if(getIdx(source.charAt(i-1)) <= 3 && getIdx(source.charAt(i)) <= 3) flag = false;
    			}
    		}
    	}
    	if(flag){
    		if(source.charAt(0) == '.') flag = false;
    		if(source.charAt(source.length() - 1) == '.') flag = false;
    		for(int i = 1; i < source.length() - 1; i++){
    			if(source.charAt(i) == '.'){
    				if((source.charAt(i-1)<'0' || source.charAt(i-1)>'9') || (source.charAt(i+1)<'0' || source.charAt(i+1)>'9')) flag = false;
    			}
    		}
    	}
    	if(!flag){
    		source = "null";
    		prefixStr = "null";
    	    infixStr = "null";
    	    suffixStr = "null";
    	}
    }
    //中缀式转后缀式
    public void infixConvertToSuffix(){
    	if(source.equals("null")) return;
        String ans = "";
        int len = source.length();
        char st[] = new char[1000]; int top = 0;
        for(int i = 0; i < len; i++){
        	if(source.charAt(i) == '('){
        		st[top++] = source.charAt(i);
        		continue;
        	}
        	if(source.charAt(i) == ')'){
        		while(st[top-1] != '('){
        			ans += st[top-1];
        			top--;
        			ans += " ";
        		}
        		top--;
        		continue;
        	}
        	if(isOpt(source.charAt(i))){
        		while(top > 0 && cmpr[getIdx(st[top-1])].charAt(getIdx(source.charAt(i))) != '<'){
        			ans += st[top-1];
                    top--;
                    ans += " ";
        		}
        		st[top++] = source.charAt(i);
        	}
        	else{
        		while(i<len && !isOpt(source.charAt(i))){
        			ans += source.charAt(i);
        			i++;
                }
        		int pointNum = 0;
        		for(int j = 0; j < ans.length(); j++){
        			if(ans.charAt(j) == '.') pointNum++;
        		}
        		if(pointNum >=2){
        			source = "null";
        			prefixStr = "null";
        			infixStr = "null";
        			suffixStr = "null";
            	    return;
        		}
                ans += " ";
        		i--;
        	}
        }
        while(top > 0){
        	ans += st[top-1];
        	top--;
        	ans += " ";
        }
        suffixStr = ans;
    }
    //根据后缀表达式建立二叉树
    public void buildTree(){
        if(source.equals("null")) return;
    	node st[] = new node[1000]; int top = 0;
    	for(int i = 0; i < 1000; i++) st[i] = new node();
    	int oldSpace = 0;
        int nowIdx = 0;
    	while(nowIdx < suffixStr.length()){
    		for(; nowIdx < suffixStr.length() && suffixStr.charAt(nowIdx) != ' '; nowIdx++);
            String str = suffixStr.substring(oldSpace, nowIdx);
    		if(isOpt(str.charAt(0))){
    			node tmp = new node(0, str.charAt(0));
    			tmp.Rson = st[--top];
    			tmp.Lson = st[--top];
    			st[top++] = tmp;
    		}
    		else{
                double temp = Double.valueOf(str);
    			st[top++] = new node(temp, (char)'@');     			
    		}
    		oldSpace = nowIdx + 1;
        	nowIdx++;
    	}
    	rt = st[0];
    	suffixStr = "";
    }
    //根据前缀表达式求值
    public void prefixCalc(){
        if(source.equals("null")) return;
        double st[] = new double[1000]; int top = 0;
        int oldSpace = prefixStr.length() - 1;    //从后往前遍历
        int nowIdx = prefixStr.length() - 2;
        while(nowIdx >= 0){
            for(; nowIdx >= 0 && prefixStr.charAt(nowIdx) != ' '; nowIdx--);
            String str = prefixStr.substring(nowIdx + 1, oldSpace);
            if(isOpt(str.charAt(0))){
                double a = st[--top];
                double b = st[--top];
                st[top++] = calc(a, str.charAt(0), b);
            }
            else{
                st[top++] = Double.valueOf(str);
            }
            oldSpace = nowIdx;
            nowIdx--;
        }
        prefixAns = st[0];
    }
    //根据中缀表达式求值
    public void infixCalc(){
        if(source.equals("null")) return;
        infixConvertToSuffix();    //中缀式转后缀式
        String tmpSuffix = suffixStr;
        double st[] = new double[1000]; int top = 0;    //通过后缀式求值
        int oldSpace = 0;
        int nowIdx = 0;
        while(nowIdx < tmpSuffix.length()) {
            for (; nowIdx < tmpSuffix.length() && tmpSuffix.charAt(nowIdx) != ' '; nowIdx++);
            String str = tmpSuffix.substring(oldSpace, nowIdx);
        	if(isOpt(str.charAt(0))){
        		double b = st[--top];
        		double a = st[--top];
        		st[top++] = calc(a, str.charAt(0), b);
        	}
        	else{
        		st[top++] = Double.valueOf(str);
        	}
        	oldSpace = nowIdx + 1;
        	nowIdx++;
        }
        infixAns = st[0];
    }
    //根据后缀表达式求值
    public void suffixCalc(){
    	if(source.equals("null")) return;
        double st[] = new double[1000]; int top = 0;
        int oldSpace = 0;    //从前往后遍历
        int nowIdx = 0;
        while(nowIdx < suffixStr.length()){
        	for(; nowIdx < suffixStr.length() && suffixStr.charAt(nowIdx) != ' '; nowIdx++);
        	String str = suffixStr.substring(oldSpace, nowIdx);
        	if(isOpt(str.charAt(0))){
        		double b = st[--top];
        		double a = st[--top];
        		st[top++] = calc(a, str.charAt(0), b);
        	}
        	else{
        		st[top++] = Double.valueOf(str);
        	}
        	oldSpace = nowIdx + 1;
        	nowIdx++;
        }
        suffixAns = st[0];
    }
    //得到二叉树的广义表形式输出
    public void getGeneralizedTabel(node rt, String s[]){
    	if(source.equals("null")) return;
        if(rt != null){
            s[cnt++] = " [";
            getGeneralizedTabel(rt.Lson, s);
            if(rt.ch == '@'){
                s[cnt++] = " "+rt.w;
            }
            else{
                s[cnt++] = " "+rt.ch;
            }
            getGeneralizedTabel(rt.Rson, s);
            s[cnt++] = " ]";
        }
    }
    //求前缀表达式
    public void prefix(node rt, String s[]){
    	if(source.equals("null")) return;
        if(rt != null){
            if(rt.ch == '@'){
            	s[cnt++] = rt.w+"";
            }
            else{
                s[cnt++] = rt.ch+"";
            }
            prefix(rt.Lson, s);
            prefix(rt.Rson, s);
        }
    }
    //求中缀表达式
    public void infix(node rt, String s[]){
    	if(source.equals("null")) return;
    	if(rt != null){
            infix(rt.Lson, s);
            if(rt.ch == '@'){
            	s[cnt++] = rt.w+"";
            }
            else{
                s[cnt++] = rt.ch+"";
            }
            infix(rt.Rson, s);
        }
    }
    //求后缀表达式
    public void suffix(node rt, String s[]){
    	if(source.equals("null")) return;
    	if(rt != null){
            suffix(rt.Lson, s);
            suffix(rt.Rson, s);
            if(rt.ch == '@'){
            	s[cnt++] = rt.w+"";
            }
            else{
                s[cnt++] = rt.ch+"";
            }
        }
    }
}