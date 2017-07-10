package com.xiaonian.sang.alibehaver.bean;


/**
 * 是时候撸点真正的代码了！！！
 * <p>
 * 创建人：桑小年
 * 日期：  2017/6/2.
 * <p>
 * 功能描述：用于在九宫格时候使用的bean，两个变量分别代表文字和图片
 */

public class GrideBean {
    public String title;
    public int imgId;


    public GrideBean(String title, int imgId) {
        this.title = title;
        this.imgId = imgId;
    }


    public GrideBean() {
    }

}
