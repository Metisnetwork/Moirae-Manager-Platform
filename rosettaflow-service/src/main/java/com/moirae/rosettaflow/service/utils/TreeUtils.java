package com.moirae.rosettaflow.service.utils;

import com.moirae.rosettaflow.mapper.domain.AlgorithmClassify;

import java.util.ArrayList;
import java.util.List;

public class TreeUtils {

    /**
     * 使用递归方法建树
     * @param treeNodes
     * @param id 树根
     * @return
     */
    public static AlgorithmClassify buildTreeByRecursive(List<AlgorithmClassify> treeNodes, Long id) {
    	if(treeNodes == null) {
    		return null;
    	}
		AlgorithmClassify tree = null;
        for (AlgorithmClassify treeNode : treeNodes) {
            if (id == treeNode.getId()) {
            	tree = treeNode;
            	findChildren(tree,treeNodes);
            }
        }
        return tree;
    }


    /**
     * 是否在树中
     * @param orgId 树根
     * @return
     */
    public static boolean isInclude(AlgorithmClassify treeNode, int orgId) {
    	if(treeNode == null) {
    		return false;
    	}
    	if(orgId == treeNode.getId()) {
    		return true;
    	}

    	List<AlgorithmClassify> childrenList = treeNode.getChildrenList();
    	if(childrenList == null) {
    		return false;
    	}

    	for (AlgorithmClassify children : childrenList) {
			if(isInclude(children, orgId)) {
				return true;
			}
		}
        return false;
    }

    /**
     * 递归查找子节点
     * @param tree
     * @param treeNodes
     */
    private static void findChildren(AlgorithmClassify tree, List<AlgorithmClassify> treeNodes) {
        for (AlgorithmClassify treeNode : treeNodes) {
            if(tree.getId().equals( treeNode.getParentId())) {
            	List<AlgorithmClassify> childrenList = tree.getChildrenList();
            	if(childrenList == null) {
            		childrenList = new ArrayList<>();
            		tree.setChildrenList(childrenList);
            	}
            	childrenList.add(treeNode);
            	findChildren(treeNode, treeNodes);
            }
        }
    }


    /**
     * 查询子树
     * @param treeNodes
     * @param id
     * @return
     */
	public static AlgorithmClassify findSubTree(AlgorithmClassify treeNodes, Long id) {
		if (treeNodes == null) {
			return null;
		}
		if (treeNodes.getId().equals(id)) {
			return treeNodes;
		}

		if(treeNodes.getChildrenList() == null){
			return null;
		}
		for (AlgorithmClassify children : treeNodes.getChildrenList()) {
			AlgorithmClassify subResult = findSubTree(children, id);
			if (subResult != null) {
				return subResult;
			}
		}
		return null;
	}

}
