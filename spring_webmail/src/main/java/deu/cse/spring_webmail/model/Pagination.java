/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package deu.cse.spring_webmail.model;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
/**
 *
 * @author ssh16
 */
@Slf4j
@RequiredArgsConstructor
public class Pagination {
    @Getter @Setter private int totalmail; //전체 메일 갯수
    @Getter @Setter private int currentpage; //보고있는 페이지
    @Getter @Setter private int postmail = 10; //페이지 당 표시할 메일 갯수
    @Getter @Setter private int postpage = 5; //표시할 페이지 갯수
    
    private int totalpage(int totalmail, int postmail){
        int totalpage = ((totalmail - 1) / postmail) +1;
        return totalpage;
    }
    
    private int endpage(int currentpage, int postpage, int totalpage){
        int endpage = (((currentpage - 1) / postpage ) + 1 ) * postpage;
        
        if(totalpage < endpage){
            endpage = totalpage;
        }
        return endpage;
    }
    
    private int startpage(int currentpage, int postpage){
        int startpage = ((currentpage - 1) / postpage ) * postpage + 1;
        
        return startpage;
    }
    
    private boolean pre(int startpage){
        return (startpage == 1) ? false : true;
    }
    
    private boolean next(int endpage, int totalpage){
        return (endpage == totalpage) ? false : true;
    }
    
    public String pagination(){
        int totalpage = totalpage(totalmail, postmail); //1
        int startpage = startpage(currentpage, postpage);  //1
        int endpage = endpage(currentpage, postpage, totalpage); //1
        boolean pre = pre(startpage); //false
        boolean next = next(endpage, totalpage); //false
        
        StringBuilder buffer = new StringBuilder();
        
        buffer.append("<ul id=pagination> ");
        if(pre){
            buffer.append("<li id=pre><a href=main_menu?currentpage=1 title=\"첫 페이지\"> |< </a></li> ");
            buffer.append("<li id=pre><a href=main_menu?currentpage=" + (currentpage - 1) + " title=\"이전 페이지\"> << </a></li> ");
        }
        for (int page = startpage; page <= endpage; page++){
            buffer.append("<li id=page><a href=main_menu?currentpage=" + page + " title=\"페이지 변경\"> " + page + "</a> </li>");
        }
        if(next){
            buffer.append("<li id=pre><a href=main_menu?currentpage=" + (currentpage + 1) + " title=\"다음 페이지\"> >> </a></li> ");
            buffer.append("<li id=last><a href=main_menu?currentpage=" + totalpage + " title=\"마지막 페이지\"> >| </a></li> ");
        }
        buffer.append("</ul>");
        
        return buffer.toString();
    }
}
