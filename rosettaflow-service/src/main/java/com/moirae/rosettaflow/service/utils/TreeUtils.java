package com.moirae.rosettaflow.service.utils;

import com.moirae.rosettaflow.mapper.domain.AlgorithmClassify;

import java.util.ArrayList;
import java.util.List;

public class TreeUtils {

    /**
     * 使用递归方法建树
     * @param treeNodes
     * @param orgId 树根
     * @return
     */
    public static AlgorithmClassify buildTreeByRecursive(List<AlgorithmClassify> treeNodes, int orgId) {
    	if(treeNodes == null) {
    		return null;
    	}
		AlgorithmClassify tree = null;
        for (AlgorithmClassify treeNode : treeNodes) {
            if (orgId == treeNode.getId()) {
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
     * 查询子树列表
     * @param treeNodes
     * @param orgId 树根
     * @return
     */
    public static List<AlgorithmClassify> buildListByRecursive(List<AlgorithmClassify> treeNodes, int orgId) {
    	if(treeNodes == null) {
    		return null;
    	}
    	List<AlgorithmClassify> result = new ArrayList<>();
        for (AlgorithmClassify treeNode : treeNodes) {
            if (orgId == treeNode.getId()) {
            	result.add(treeNode);
            	findChildrenList(treeNode,treeNodes,result);
            }
        }
        return result;
    }

    /**
     * 递归查找子节点
     * @param treeNodes
     */
    public static void findChildrenList(AlgorithmClassify parentNode, List<AlgorithmClassify> treeNodes, List<AlgorithmClassify> result) {
    	for (AlgorithmClassify treeNode : treeNodes) {
            if(parentNode.getId().equals( treeNode.getParentId())) {
            	result.add(treeNode);
            	findChildrenList(treeNode,treeNodes,result);
            }
        }
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
     * @param orgId
     * @return
     */
	public static AlgorithmClassify findSubTree(AlgorithmClassify treeNodes, Integer orgId) {
		if (treeNodes == null) {
			return null;
		}
		if (treeNodes.getId().equals(orgId)) {
			return treeNodes;
		}

		for (AlgorithmClassify children : treeNodes.getChildrenList()) {
			AlgorithmClassify subResult = findSubTree(children, orgId);
			if (subResult != null) {
				return subResult;
			}
		}
		return null;
	}

}
