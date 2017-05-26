
public class Test2 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BSTStringsPlus t = new BSTStringsPlus();
		
		t.insert("fall");
		t.insert("ball");
		t.insert("shut");
		t.insert("all");
		t.insert("call");
		t.insert("cab");
		t.insert("car");
		t.insert("zap");
		t.insert("zbp");
		t.insert("zcp");
		t.insert("azz");
		t.insert("axx");
		t.insert("sha");
		System.out.println(t.toString());
		System.out.println(t.closeness());
		System.out.println(t.height());
		System.out.println(t.secondSmallest().getString());
		System.out.println(t.leafCt());
		System.out.println(t.jointChildCt());
		t.delete("zap");
		System.out.println(t.toString());
		t.rotateLeft("car");
		System.out.println(t.toString());
		
	}

}
