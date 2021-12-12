package com.example.demo.tests;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class APP01 {
    public int[] massTransform(int[] sourceMass){
        List<Integer> sourceList = Arrays.stream(sourceMass).boxed().collect(Collectors.toList());
        if(!sourceList.contains(4)){
            throw new RuntimeException();
        }
        int index = sourceList.lastIndexOf(4);
        if (index==sourceList.size()-1){
            return new int[]{};
        }
        int in=0;
        int[] res= new int[sourceList.size()-index-1];
        for (int a = index+1;a<sourceList.size();a++){
            res[in]=sourceList.get(a);
            in++;
        }
        return res;
    }

    public boolean massCheck(int[] sourceMass){
        List<Integer> sourceList = Arrays.stream(sourceMass).boxed().collect(Collectors.toList());
        if(sourceList.contains(4)&& sourceList.contains(1)){
            for (Integer a:sourceList) {
                if(a!=1 && a!=4){
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
