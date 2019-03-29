package com.ldz.sys.bean;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * auther chenwei
 * create at 2018/5/5
 */
@Getter
@Setter
public class TreeNode {
    private String label;
    private String value;
    private String father;
    private List<TreeNode> children;


    public static List<TreeNode> buildTree(List<TreeNode> list){
        Map<String,TreeNode> nodeMap = list.stream().collect(Collectors.toMap(TreeNode::getValue, p->p));
        List<TreeNode> root = new ArrayList<>();
        for (TreeNode node : list) {
            if (StringUtils.isEmpty(node.getFather()) || !nodeMap.containsKey(node.getFather())){
                root.add(node);
                continue;
            }
            TreeNode father = nodeMap.get(node.getFather());
            if (father == null)continue;
            if (father.getChildren() == null){
                List<TreeNode> children = new ArrayList<>();
                children.add(node);
                father.setChildren(children);
            }else{
                father.getChildren().add(node);
            }
        }
        return root;
    }
}
