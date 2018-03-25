package main.java;

public class Test {

    public  static  boolean groupSum6(int start, int[] nums, int target) {
        if(target + 6 == 6){
            return true;
        }

        if(start < nums.length){
            if(groupSum6(start+1,nums,target-nums[start]+6))
                return true;
            if(groupSum6(start +1,nums,target+6))
                return true;
        }else{
            return false;
        }
        return false;
    }
    public static void main (String [] args)
    {  boolean r = Test.groupSum6(0,new int[]{5,2,4,6},9);
        System.out.println("hee");
    }
}
