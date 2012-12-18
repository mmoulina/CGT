package com.poplicus.crawler.codegen.gui.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import com.poplicus.crawler.codegen.gui.components.PopTreeNode.PopTreeNodeType;

public class PopTreePopup extends JPopupMenu {
	
	private static final long serialVersionUID = -6760495019631296294L;
	
	private static FontManager fontManager = FontManager.getInstance();
	
	private PopTree rootTree = null;
	
	private String uniqueNameOfWebPage = null;
	
	private String browserTypeOfWebPage = null;
	
	private String webPageName = null;
	
	private String sourceID = null;
	
	private PopTreeNode parentNode = null;
	
	private JMenuItem miIf_root = null;
	private JMenuItem miElseIf_root = null;
	private JMenuItem miElse_root = null;
	private JMenuItem miLineOfCode_root = null;
	//private JMenuItem miAction_root = null;

	private JMenuItem miIf_if = null;
	private JMenuItem miElseIf_if = null;
	private JMenuItem miElse_if = null;
	private JMenu miNestedCondition_if = null;
	private JMenuItem miNestedIf_if = null;
	private JMenuItem miLineOfCode_if = null;
	//private JMenuItem miAction_if = null;
	
	private boolean disable_if_root = false;
	private boolean disable_elseif_root = false;
	private boolean disable_else_root = false;
	
	private boolean disable_if_if = false;
	private boolean disable_elseif_if = false;
	private boolean disable_else_if = false;
	
	private PopTreeNode peerIFNode = null;
	
	public PopTreePopup() {
		super();
		this.setFont(fontManager.getPopupFontPlain());
		initializeRootPopup();
	}
	
	public PopTreePopup(String label) {
		super(label);
		this.setFont(fontManager.getPopupFontPlain());
		initializeRootPopup();
	}
	
	public PopTreePopup(boolean ifPopup) {
		super();
		this.setFont(fontManager.getPopupFontPlain());
		if(ifPopup) {
			initializeIfPopup();
		} else {
			initializeRootPopup();
		}
	}
	
	public PopTreePopup(String label, boolean ifPopup) {
		super(label);
		this.setFont(fontManager.getPopupFontPlain());
		if(ifPopup) {
			initializeIfPopup();
		} else {
			initializeRootPopup();	
		}
	}

	public String getWebPageName() {
		return webPageName;
	}

	public void setWebPageName(String webPageName) {
		this.webPageName = webPageName;
	}

	private void initializeRootPopup() {
		miIf_root = new JMenuItem("If");
		miIf_root.setName("PeerIf");
		miIf_root.setFont(fontManager.getMenuItemFontPlain());
		miIf_root.addActionListener(new MenuItemActionListener());
		
		miElseIf_root = new JMenuItem("Else-If");
		miElseIf_root.setName("PeerElseIf");
		miElseIf_root.setFont(fontManager.getMenuItemFontPlain());
		miElseIf_root.addActionListener(new MenuItemActionListener());
		miElseIf_root.setEnabled(false);
		
		miElse_root = new JMenuItem("Else");
		miElse_root.setName("PeerElse");
		miElse_root.setFont(fontManager.getMenuItemFontPlain());
		miElse_root.addActionListener(new MenuItemActionListener());
		miElse_root.setEnabled(false);
		
		miLineOfCode_root = new JMenuItem("Line of Code");
		miLineOfCode_root.setName("LineOfCode");
		miLineOfCode_root.setFont(fontManager.getMenuItemFontPlain());
		miLineOfCode_root.addActionListener(new MenuItemActionListener());

//		miAction_root = new JMenuItem("Action");
//		miAction_root.setName("Action");
//		miAction_root.setFont(fontManager.getMenuItemFontPlain());
//		miAction_root.addActionListener(new MenuItemActionListener());
		
		this.add(miIf_root);
		this.add(miElseIf_root);
		this.add(miElse_root);
		this.addSeparator();
		this.add(miLineOfCode_root);
//		this.addSeparator();
//		this.add(miAction_root);
	}
	
	private void initializeIfPopup() {
		miIf_if = new JMenuItem("If");
		miIf_if.setName("PeerIf");
		miIf_if.setFont(fontManager.getMenuItemFontPlain());
		miIf_if.addActionListener(new MenuItemActionListener());
		miIf_if.setEnabled(false);
		
		miElseIf_if = new JMenuItem("Else-If");
		miElseIf_if.setName("PeerElseIf");
		miElseIf_if.setFont(fontManager.getMenuItemFontPlain());
		miElseIf_if.addActionListener(new MenuItemActionListener());
		
		miElse_if = new JMenuItem("Else");
		miElse_if.setName("PeerElse");
		miElse_if.setFont(fontManager.getMenuItemFontPlain());
		miElse_if.addActionListener(new MenuItemActionListener());
		
		miNestedCondition_if = new JMenu("Nested");
		miNestedCondition_if.setFont(fontManager.getMenuItemFontPlain());
		
		miNestedIf_if = new JMenuItem("If");
		miNestedIf_if.setName("NestedIf");
		miNestedIf_if.setFont(fontManager.getMenuItemFontPlain());
		miNestedIf_if.addActionListener(new MenuItemActionListener());
		miNestedCondition_if.add(miNestedIf_if);

		miLineOfCode_if = new JMenuItem("Line of Code");
		miLineOfCode_if.setName("LineOfCode");
		miLineOfCode_if.setFont(fontManager.getMenuItemFontPlain());
		miLineOfCode_if.addActionListener(new MenuItemActionListener());
		miNestedCondition_if.add(miLineOfCode_if);

//		miAction_if = new JMenuItem("Action");
//		miAction_if.setName("Action");
//		miAction_if.setFont(fontManager.getMenuItemFontPlain());
//		miAction_if.addActionListener(new MenuItemActionListener());
		
		this.add(miIf_if);
		this.add(miElseIf_if);
		this.add(miElse_if);
		this.add(miNestedCondition_if);
//		this.addSeparator();
//		this.add(miLineOfCode_if);
//		this.addSeparator();
//		this.add(miAction_if);
	}

	
	private PopConditionFrame getConditionPanel() {
		PopConditionFrame conFrame = new PopConditionFrame();
		conFrame.setSize(650, 505);
		conFrame.setLocation(150, 100);
		
		return conFrame;
	}
	
	private PopConditionFrame launchConditionalPanelForIfCondition(PopTreeNodeType nodeType) {
		rootTree.getContainingFrame().setEnabled(false);
		PopConditionFrame.rootTree = rootTree;
		PopConditionFrame conFrame = getConditionPanel();
		conFrame.setNodeType(nodeType);
		conFrame.setUniqueNameOfWebPage(uniqueNameOfWebPage);
		conFrame.setWebPageName(webPageName);
		conFrame.setBrowserTypeOfWebPage(browserTypeOfWebPage);
		conFrame.setSourceID(sourceID);
		if(nodeType.equals(PopTreeNodeType.Else_If) || nodeType.equals(PopTreeNodeType.Else)) {
			conFrame.setParentNode((PopTreeNode) parentNode.getParent());
			if(peerIFNode != null) {
				conFrame.setPeerIfNode(peerIFNode);
			}
		} else {
			conFrame.setParentNode(parentNode);	
		}
		
		return conFrame;
	}

	private PopLoCFrame getLineOfCodePanel() {
		PopLoCFrame locFrame = new PopLoCFrame(browserTypeOfWebPage, uniqueNameOfWebPage, sourceID);
		locFrame.setSize(850, 605);
		locFrame.setLocation(150, 100);
		
		return locFrame;
	}

	private PopLoCFrame launchLoCPanel(PopTreeNodeType nodeType) {
		rootTree.getContainingFrame().setEnabled(false);
		PopLoCFrame.rootTree = rootTree;
		PopLoCFrame locFrame = getLineOfCodePanel();
		locFrame.setNodeType(nodeType);
		//locFrame.setUniqueNameOfWebPage(uniqueNameOfWebPage);
		locFrame.setWebPageName(webPageName);
		//locFrame.setSourceID(sourceID);
		locFrame.setParentNode(parentNode);	
//		if(nodeType.equals(PopTreeNodeType.Else_If) || nodeType.equals(PopTreeNodeType.Else)) {
//			locFrame.setParentNode((PopTreeNode) parentNode.getParent());
//			if(peerIFNode != null) {
//				locFrame.setPeerIfNode(peerIFNode);
//			}
//		} else {
//			locFrame.setParentNode(parentNode);	
//		}
		
		return locFrame;
	}

	public PopTree getRootTree() {
		return rootTree;
	}

	public void setRootTree(PopTree rootTree) {
		this.rootTree = rootTree;
	}
	
	public String getUniqueNameOfWebPage() {
		return uniqueNameOfWebPage;
	}

	public void setUniqueNameOfWebPage(String uniqueNameOfWebPage) {
		this.uniqueNameOfWebPage = uniqueNameOfWebPage;
	}
	
	public String getBrowserTypeOfWebPage() {
		return browserTypeOfWebPage;
	}

	public void setBrowserTypeOfWebPage(String browserTypeOfWebPage) {
		this.browserTypeOfWebPage = browserTypeOfWebPage;
	}

	public String getSourceID() {
		return sourceID;
	}

	public void setSourceID(String sourceID) {
		this.sourceID = sourceID;
	}
	
	public PopTreeNode getParentNode() {
		return parentNode;
	}

	public void setParentNode(PopTreeNode parentNode) {
		this.parentNode = parentNode;
	}
	
	public boolean isDisableIfRoot() {
		return disable_if_root;
	}

	public void setDisableIf_Root(boolean disable_if_root) {
		this.disable_if_root = disable_if_root;
		this.miIf_root.setEnabled(disable_if_root);
	}

	public boolean isDisableElseIf_Root() {
		return disable_elseif_root;
	}

	public void setDisableElseIf_Root(boolean disable_elseif_root) {
		this.disable_elseif_root = disable_elseif_root;
		this.miElseIf_root.setEnabled(disable_elseif_root);
	}

	public boolean isDisableElse_Root() {
		return disable_else_root;
	}

	public void setDisableElse_Root(boolean disable_else_root) {
		this.disable_else_root = disable_else_root;
		this.miElse_root.setEnabled(disable_else_root);
	}

	public boolean isDisableIf_If() {
		return disable_if_if;
	}

	public void setDisableIf_If(boolean disable_if_if) {
		this.disable_if_if = disable_if_if;
		this.miIf_if.setEnabled(disable_if_if);
	}

	public boolean isDisableElseIf_If() {
		return disable_elseif_if;
	}

	public void setDisableElseIf_If(boolean disable_elseif_if) {
		this.disable_elseif_if = disable_elseif_if;
		this.miElseIf_if.setEnabled(disable_elseif_if);
	}

	public boolean isDisableElse_If() {
		return disable_else_if;
	}

	public void setDisableElse_If(boolean disable_else_if) {
		this.disable_else_if = disable_else_if;
		this.miElse_if.setEnabled(disable_else_if);
	}

	public PopTreeNode getPeerIFNode() {
		return peerIFNode;
	}

	public void setPeerIFNode(PopTreeNode peerIFNode) {
		this.peerIFNode = peerIFNode;
	}

	class MenuItemActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg) {
			JMenuItem menuItem = (JMenuItem) arg.getSource();
			if(menuItem.getName().equals("PeerIf")) {
				launchConditionalPanelForIfCondition(PopTreeNodeType.If).setVisible(true);
			} else if(menuItem.getName().equals("PeerElseIf")) {
				launchConditionalPanelForIfCondition(PopTreeNodeType.Else_If).setVisible(true);
			} else if(menuItem.getName().equals("PeerElse")) {
				//launchConditionalPanelForIfCondition(PopTreeNodeType.Else).setVisible(true);
				PopConditionFrame conFrame = launchConditionalPanelForIfCondition(PopTreeNodeType.Else);
				conFrame.addElseToTree(conFrame);
			} else if(menuItem.getName().equals("NestedIf")) {
				launchConditionalPanelForIfCondition(PopTreeNodeType.If).setVisible(true);
			} else if(menuItem.getName().equals("LineOfCode")) {
				launchLoCPanel(PopTreeNodeType.LineOfCode).setVisible(true);
			}/*  else if(menuItem.getName().equals("Action")) {
				
			}*/
			
		}
		
	}
	
}
