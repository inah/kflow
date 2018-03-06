package fmcr.visitors;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.ArrayCreationLevel;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.AnnotationDeclaration;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.ReceiverParameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.comments.BlockComment;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.expr.ArrayAccessExpr;
import com.github.javaparser.ast.expr.ArrayCreationExpr;
import com.github.javaparser.ast.expr.ArrayInitializerExpr;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.expr.BooleanLiteralExpr;
import com.github.javaparser.ast.expr.CastExpr;
import com.github.javaparser.ast.expr.CharLiteralExpr;
import com.github.javaparser.ast.expr.ClassExpr;
import com.github.javaparser.ast.expr.ConditionalExpr;
import com.github.javaparser.ast.expr.DoubleLiteralExpr;
import com.github.javaparser.ast.expr.EnclosedExpr;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.InstanceOfExpr;
import com.github.javaparser.ast.expr.IntegerLiteralExpr;
import com.github.javaparser.ast.expr.LambdaExpr;
import com.github.javaparser.ast.expr.LongLiteralExpr;
import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.expr.MemberValuePair;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.NormalAnnotationExpr;
import com.github.javaparser.ast.expr.NullLiteralExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.expr.SingleMemberAnnotationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.SuperExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.expr.TypeExpr;
import com.github.javaparser.ast.expr.UnaryExpr;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.modules.ModuleDeclaration;
import com.github.javaparser.ast.modules.ModuleExportsStmt;
import com.github.javaparser.ast.modules.ModuleOpensStmt;
import com.github.javaparser.ast.modules.ModuleProvidesStmt;
import com.github.javaparser.ast.modules.ModuleRequiresStmt;
import com.github.javaparser.ast.modules.ModuleUsesStmt;
import com.github.javaparser.ast.stmt.AssertStmt;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.BreakStmt;
import com.github.javaparser.ast.stmt.CatchClause;
import com.github.javaparser.ast.stmt.ContinueStmt;
import com.github.javaparser.ast.stmt.DoStmt;
import com.github.javaparser.ast.stmt.EmptyStmt;
import com.github.javaparser.ast.stmt.ExplicitConstructorInvocationStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ForStmt;
import com.github.javaparser.ast.stmt.ForeachStmt;
import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.LabeledStmt;
import com.github.javaparser.ast.stmt.LocalClassDeclarationStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.SwitchEntryStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.stmt.TryStmt;
import com.github.javaparser.ast.stmt.UnparsableStmt;
import com.github.javaparser.ast.stmt.WhileStmt;
import com.github.javaparser.ast.type.ArrayType;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.IntersectionType;
import com.github.javaparser.ast.type.PrimitiveType;
import com.github.javaparser.ast.type.TypeParameter;
import com.github.javaparser.ast.type.UnionType;
import com.github.javaparser.ast.type.UnknownType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.ast.type.WildcardType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.declarations.ParameterDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.Type;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import fmcr.display.TypeInputSourcesDialog;
import fmcr.leaks.detectors.CascadeObjectCreation;
import fmcr.leaks.detectors.CascadeObjectCreationLeak;
import fmcr.leaks.detectors.ClassCascadingObjectCreationLeak;
import fmcr.leaks.detectors.ClassMethodArgAsObjectFieldReferenceLeak;
import fmcr.leaks.detectors.ClassMethodArgAsObjectLeak;
import fmcr.leaks.detectors.ClassMethodArgAsObjectMethodCallLeak;
import fmcr.leaks.detectors.ClassMethodCallLeak;
import fmcr.leaks.detectors.ClassObjectCreationLeak;
import fmcr.leaks.detectors.ClassObjectFieldAccessLeak;
import fmcr.leaks.detectors.ClassVariableAccessLeak;
import fmcr.leaks.detectors.Leak;
import fmcr.leaks.detectors.MethodArgAsObjectFieldReferenceLeak;
import fmcr.leaks.detectors.MethodArgAsObjectLeak;
import fmcr.leaks.detectors.MethodCallLeak;
import fmcr.leaks.detectors.ObjectFieldAccessLeak;
import fmcr.leaks.detectors.Tag;
import fmcr.leaks.detectors.MethodArgAsObjectMethodCallLeak;
import fmcr.main.Client;

/**
 * A visitor that traverse an AST to extract its program attributes and determine the
 * existence of the following bugs:
 * <pre>
 * (1)
 * </pre>
 *
 * @author  Inah Omoronyia 
 * @version 1.0
 * 
 */
public class CodeVisitor extends VoidVisitorAdapter<String>{
	int counter;
	private static HashMap<String,String> vertexCodeAttributions;
	private static HashMap<String, ArrayList<String>> vertextChildren;
	private static HashMap<String, String> vertexParent;
	public static String className;
		
	CombinedTypeSolver ts;
	public static ArrayList<Leak>leaks;
	private HashMap<String, String> createdObjectTypes;
	
	public CodeVisitor() {
		counter = 0;
		vertexCodeAttributions = new HashMap<String,String>();
		vertextChildren = new HashMap<String, ArrayList<String>>();
		vertexParent = new HashMap<String,String>();	
		
		ts =  setupTypeSolver();
		leaks = new ArrayList<Leak>();
		createdObjectTypes = new HashMap<String,String>();
	}
	
	
	@Override
	public void visit(AnnotationDeclaration n, String arg) {// arg represents the parent of the visited node
		
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "AnnotationDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
		
	}
	
	@Override
	public void visit(AnnotationMemberDeclaration n, String arg) {
		
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "AnnotationMemberDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ArrayAccessExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ArrayAccessExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ArrayCreationExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ArrayCreationExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ArrayInitializerExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ArrayInitializerExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(AssertStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "AssertStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(AssignExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "AssignExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(BinaryExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "BinaryExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(BlockComment n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "BlockComment");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(BlockStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "BlockStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(BooleanLiteralExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "BooleanLiteralExpr:"+n.toString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(BreakStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "BreakStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(CastExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "CastExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(CatchClause n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "CatchClause");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(CharLiteralExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "CharLiteralExpr:"+n.toString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ClassExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ClassExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ClassOrInterfaceDeclaration n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ClassOrInterfaceDeclaration:");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		className = n.getNameAsString();
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ClassOrInterfaceType n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ClassOrInterfaceType");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(CompilationUnit n, String arg) {

		String id = "ast"+counter;
		
		addVertexCodeAttribution(id,n.toString());
		
		Client.getDisplay().astview.createVertex(id, "CompilationUnit");		
		counter = counter+1;
//		Client.getDisplay().astview.addEdge(arg, child);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ConditionalExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ConditionalExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
		
	}
	
	@Override
	public void visit(ConstructorDeclaration n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ConstructorDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ContinueStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ContinueStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(DoStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "DoStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(DoubleLiteralExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "DoubleLiteralExpr:"+n.toString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(EmptyStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "EmptyStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(EnclosedExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "EnclosedExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(EnumConstantDeclaration n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "EnumConstantDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(EnumDeclaration n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "EnumDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);		
	}
	
	@Override
	public void visit(ExplicitConstructorInvocationStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ExplicitConstructorInvocationStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ExpressionStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ExpressionStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(FieldAccessExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;
		
		String sourceCode = n.toString();

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "FieldAccessExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		try {
			String fieldHandlerType = null;
			String fieldHandlerName = null;
			String fieldType = null;
			String fieldName = null;
			
			/*
			 * Object field reference
			 * Example:
			 * PayInfo p = new PayInfo(new Double(1.99));
			 * WorkInfo w = new WorkInfo(new  Double(5));		
			 * w.hrsworked"|WorkInfo.hrsworked
			 */
			List<Node> children = n.getChildNodes();
			for(Node c:children){
				if(c instanceof NameExpr) {
					NameExpr ne = (NameExpr)c;
					boolean solvable = JavaParserFacade.get(ts).solve(ne).isSolved();//checks whether static or instance variable call
					if(solvable) {
						fieldHandlerName = JavaParserFacade.get(ts).solve(ne).getCorrespondingDeclaration().getName();
						fieldHandlerType = JavaParserFacade.get(ts).solve(ne).getCorrespondingDeclaration().getType().describe();
						if(fieldHandlerType.contains(".")) {
							String[] splitpkgn = fieldHandlerType.split("\\.");
							fieldHandlerType = splitpkgn[splitpkgn.length-1];
						}
					}			
					else {
						fieldHandlerType = ((NameExpr) c).getName().asString();
					}
				}
				else if(c instanceof SimpleName) {
					SimpleName ne = (SimpleName)c;
					fieldName = ne.asString();
				}
				
			}			
			String leakline = n.getRange().get().toString();
			ObjectFieldAccessLeak ofal = new ObjectFieldAccessLeak(id,fieldHandlerName,fieldHandlerType, fieldName, fieldType,leakline);
			ofal.setCodeSource(sourceCode);
			leaks.add(ofal);
			updateKnowledgeGraph(ofal);
			ClassObjectFieldAccessLeak cofal = new ClassObjectFieldAccessLeak(id,CodeVisitor.className, fieldHandlerName,fieldHandlerType, fieldName, fieldType,leakline);
			cofal.setCodeSource(sourceCode);
			leaks.add(cofal);
			updateKnowledgeGraph(cofal);

			/*
			 * Method parameters containing Object method calls leaks
			 * Example:
			 * PayInfo p = new PayInfo(new Double(1.99));
			 * WorkInfo w = new WorkInfo(new  Double(5));
			 * double salary = p.computeSalary(w.hrsworked|WorkInfo.hrsworked);
			 */
			
			String parentLabel = Client.getDisplay().astview.getVertexLabel(arg);
			if(parentLabel.equals("MethodCallExpr")) {
				Optional<Node> pn =n.getParentNode();
				MethodCallExpr pmce =(MethodCallExpr)pn.get();
				String reference_t = JavaParserFacade.get(ts).solve(pmce).getCorrespondingDeclaration().declaringType().getName();
				String return_t = JavaParserFacade.get(ts).solve(pmce).getCorrespondingDeclaration().getReturnType().describe();
				String method_name = JavaParserFacade.get(ts).solve(pmce).getCorrespondingDeclaration().getName();
				MethodArgAsObjectFieldReferenceLeak maaofrl = new MethodArgAsObjectFieldReferenceLeak(id,reference_t,method_name, return_t,fieldHandlerName,fieldHandlerType, fieldType,fieldName,leakline);
				maaofrl.setCodeSource(sourceCode);
				leaks.add(maaofrl);
				updateKnowledgeGraph(maaofrl);
				ClassMethodArgAsObjectFieldReferenceLeak cmaaofrl = new ClassMethodArgAsObjectFieldReferenceLeak(id,CodeVisitor.className,reference_t,method_name, return_t,fieldHandlerName,fieldHandlerType, fieldType,fieldName,leakline);
				cmaaofrl.setCodeSource(sourceCode);
				leaks.add(cmaaofrl);
				updateKnowledgeGraph(cmaaofrl);
			}
			
			/*
			 * Object creation with method call parameters
			 * Example:
			 * PayInfo p = new PayInfo(w.getHrsworked());
			 */

			String parentId = Client.getDisplay().astview.getParentIdentifier(id);
			String parentLabelLoc = Client.getDisplay().astview.getVertexLabel(parentId);
			if(parentLabelLoc.contains("ObjectCreationExpr")) {
				ClassCascadingObjectCreationLeak cascadingLeak = new ClassCascadingObjectCreationLeak(id, CodeVisitor.className, leakline);
				while(parentLabelLoc.equals("ObjectCreationExpr")) {
					String containingObjectType = createdObjectTypes.get(parentId);
//					CascadeObjectCreation cascade = new CascadeObjectCreation(id,parentId,null,fieldHandlerType, null,containingObjectType, leakline);
//					CascadeObjectCreation cascade = new CascadeObjectCreation(id,parentId,null,fieldName, null,containingObjectType, leakline);
//					CascadeObjectCreation cascade = new CascadeObjectCreation(id,parentId,null,fieldHandlerType+":"+fieldName, null,containingObjectType, leakline);
					
					CascadeObjectCreation cascade1 = new CascadeObjectCreation(id,parentId,null,fieldHandlerType, null,containingObjectType, leakline);
					CascadeObjectCreation cascade2 = new CascadeObjectCreation(id,parentId,null,fieldName, null,fieldHandlerType, leakline);
					
					cascadingLeak.addObjectCreation(cascade1);
					cascadingLeak.addObjectCreation(cascade2);
					
					parentId = Client.getDisplay().astview.getParentIdentifier(parentId);//get grandparent
					parentLabelLoc = Client.getDisplay().astview.getVertexLabel(parentId);					
				}
					
				ArrayList<CascadeObjectCreationLeak> cascadeLeaks = cascadingLeak.extractLeaks();
				for(CascadeObjectCreationLeak leak: cascadeLeaks) {
					leak.setCodeSource(sourceCode);
					leaks.add(leak);	
					updateKnowledgeGraph(leak);
				}
			}
			//
//			

		}
		catch (UnsolvedSymbolException e) {
			Client.getDisplay().updateLogPage("faUnsolvable: "+n.toString(), true);
		}catch(RuntimeException e) {
			Client.getDisplay().updateLogPage("farUnsolvable: "+e.getMessage(), true);
		}
		
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(FieldDeclaration n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "FieldDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ForeachStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ForeachStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ForStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ForStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(IfStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "IfStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(InitializerDeclaration n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "InitializerDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(InstanceOfExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "InstanceOfExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(IntegerLiteralExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "IntegerLiteralExpr:"+n.toString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(JavadocComment n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "JavadocComment");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
		
	}
	
	@Override
	public void visit(LabeledStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "LabeledStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(LineComment n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "LineComment");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(LongLiteralExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;
		
		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);
		
		Object child = Client.getDisplay().astview.createVertex(id, "LongLiteralExpr:"+n.toString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(MarkerAnnotationExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "MarkerAnnotationExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);		
	}
	
	@Override
	public void visit(MemberValuePair n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "MemberValuePair");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(MethodCallExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;
		
		String sourceCode = n.toString();

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "MethodCallExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		try {
			// attempt to get type
//			Type type = JavaParserFacade.get(ts).getType(n);
//			String reference_tx = type.describe();
			
			String reference_t = JavaParserFacade.get(ts).solve(n).getCorrespondingDeclaration().declaringType().getName();
			String return_t = JavaParserFacade.get(ts).solve(n).getCorrespondingDeclaration().getReturnType().describe();
			String method_name = JavaParserFacade.get(ts).solve(n).getCorrespondingDeclaration().getName();
//			String method_handler = JavaParserFacade.get(ts).solve(n).getCorrespondingDeclaration().getQualifiedName();

			/*
			 * Object method calls leaks
			 * Example:
			 * double salary = p.computeSalary(xxx);
			 */
			String leakline = n.getRange().get().toString();
			MethodCallLeak mcl = new MethodCallLeak(id,reference_t,method_name, return_t, leakline);
			mcl.setCodeSource(sourceCode);
			leaks.add(mcl);
			updateKnowledgeGraph(mcl);
			ClassMethodCallLeak cmcl = new ClassMethodCallLeak(id,CodeVisitor.className,reference_t,method_name, return_t, leakline);
			cmcl.setCodeSource(sourceCode);
			leaks.add(cmcl);
			updateKnowledgeGraph(cmcl);

			/*
			 * Method parameters containing Object method calls leaks
			 * Example:
			 * PayInfo p = new PayInfo(new Double(1.99));
			 * WorkInfo w = new WorkInfo(new  Double(5));
			 * double salary = p.computeSalary(w.getHrsworked());
			 */
			String parentLabel = Client.getDisplay().astview.getVertexLabel(arg);
			if(parentLabel.equals("MethodCallExpr")) {
				String mhandle = "";
				for(Leak mch_:leaks) {
					if(mch_.getNodeId().equals(arg)) {
						if(mch_ instanceof MethodCallLeak) {
							MethodCallLeak mcl_t = (MethodCallLeak)mch_;
							mhandle = mcl_t.getHandlerType();
							break;
						}
						
					}
				}
				MethodArgAsObjectMethodCallLeak mcpl = new MethodArgAsObjectMethodCallLeak(id,reference_t,method_name, return_t, leakline,mhandle);
				mcpl.setCodeSource(sourceCode);
				leaks.add(mcpl);
				updateKnowledgeGraph(mcpl);
				ClassMethodArgAsObjectMethodCallLeak cmcpl = new ClassMethodArgAsObjectMethodCallLeak(id,CodeVisitor.className,reference_t,method_name, return_t, leakline,mhandle);
				cmcpl.setCodeSource(sourceCode);
				leaks.add(cmcpl);
				updateKnowledgeGraph(cmcpl);
				
			}
			
			/*
			 * Method parameters containing Object leaks
			 * Example:
			 * PayInfo p = new PayInfo(new Double(1.99));
			 * WorkInfo w = new WorkInfo(new  Double(5));
			 * double salary = p.computeSalary(w);
			 */
			int no_params = JavaParserFacade.get(ts).solve(n).getCorrespondingDeclaration().getNumberOfParams();
			for(int i=0;i<no_params;i++) {
				ParameterDeclaration param = JavaParserFacade.get(ts).solve(n).getCorrespondingDeclaration().getParam(i);
				String paramName = param.getName();
				String paramType = param.describeType();				
				if(!(param.getType().isPrimitive()||
						paramType.startsWith("java.") ||
						paramType.startsWith("javax.") ||
						paramType.startsWith("org.omg.") ||
						paramType.startsWith("org.w3c.dom.") ||
						paramType.startsWith("org.xml.sax."))) {
					
					if(paramType.contains(".")) {
						String[] splitpkgn = paramType.split("\\.");
						paramType = splitpkgn[splitpkgn.length-1];
					}
					MethodArgAsObjectLeak maaol = new MethodArgAsObjectLeak(id,reference_t,paramType,paramName,leakline);
					maaol.setCodeSource(sourceCode);
					leaks.add(maaol);
					updateKnowledgeGraph(maaol);
					ClassMethodArgAsObjectLeak cmaaol = new ClassMethodArgAsObjectLeak(id,CodeVisitor.className,reference_t,paramType,paramName,leakline);
					cmaaol.setCodeSource(sourceCode);
					leaks.add(cmaaol);	
					updateKnowledgeGraph(cmaaol);
				}
						
			}
			

			
			/*
			 * Object creation with method call parameters
			 * Example:
			 * PayInfo p = new PayInfo(w.getHrsworked());
			 */

			String parentId = Client.getDisplay().astview.getParentIdentifier(id);
			String parentLabelLoc = Client.getDisplay().astview.getVertexLabel(parentId);
			if(parentLabelLoc.contains("ObjectCreationExpr")) {
				ClassCascadingObjectCreationLeak cascadingLeak = new ClassCascadingObjectCreationLeak(id, CodeVisitor.className, leakline);
				cascadingLeak.tag = Tag.F;
				while(parentLabelLoc.equals("ObjectCreationExpr")) {
					String containingObjectType = createdObjectTypes.get(parentId);
					CascadeObjectCreation cascade1 = new CascadeObjectCreation(id,parentId,null,reference_t, null,containingObjectType, leakline);
					CascadeObjectCreation cascade2 = new CascadeObjectCreation(id,parentId,null,method_name, null,reference_t, leakline);
//					CascadeObjectCreation cascade = new CascadeObjectCreation(id,parentId,null,reference_t, null,containingObjectType, leakline);
					//CascadeObjectCreation cascade = new CascadeObjectCreation(id,parentId,null,fieldHandlerType+":"+fieldName, null,containingObjectType, leakline);
					cascadingLeak.addObjectCreation(cascade1);
					cascadingLeak.addObjectCreation(cascade2);
					
					parentId = Client.getDisplay().astview.getParentIdentifier(parentId);//get grandparent
					parentLabelLoc = Client.getDisplay().astview.getVertexLabel(parentId);
					
				}
					
				ArrayList<CascadeObjectCreationLeak> cascadeLeaks = cascadingLeak.extractLeaks();
				for(CascadeObjectCreationLeak leak: cascadeLeaks) {
					leak.tag = Tag.F;
					leak.setCodeSource(sourceCode);
					leaks.add(leak);	
					updateKnowledgeGraph(leak);
				}
			}
			//

		}
		catch (UnsolvedSymbolException e) {
			Client.getDisplay().updateLogPage("mUnsolvable: "+n.toString(), true);
		}catch(RuntimeException e) {
			Client.getDisplay().updateLogPage("mrUnsolvable: "+e.getMessage(), true);
		}

		super.visit(n, id);
	}
	
	@Override
	public void visit(MethodDeclaration n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "MethodDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(NameExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;
		
		String sourceCode = n.toString();

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "NameExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		try {
			// attempt to get type
			Type type = JavaParserFacade.get( ts ).getType( n );
			String reference_tx = type.describe();
			if(!(type.isPrimitive()||
				 reference_tx.startsWith("java.") ||
			     reference_tx.startsWith("javax.") ||
			     reference_tx.startsWith("org.omg.") ||
  			     reference_tx.startsWith("org.w3c.dom.") ||
			     reference_tx.startsWith("org.xml.sax."))) {
				
				/*
				 * Object creation leak (without parameters)
				 * Example:
				 * 	Class Payslip{ public main(){p.computeSalary();}
				 */
				Type t = JavaParserFacade.get(ts).solve(n).getCorrespondingDeclaration().getType();
				String desc = t.describe();
				String accessVariableType = "";
				if(desc.contains(".")) {
					String [] t_name1 = desc.split("\\.");
					accessVariableType = t_name1[t_name1.length-1];	
				}
				else {
					accessVariableType = desc;
				}
				String leakline = n.getRange().get().toString();
				ClassVariableAccessLeak cocl = new ClassVariableAccessLeak(id,null,accessVariableType, CodeVisitor.className, leakline);
				cocl.setCodeSource(sourceCode);
				leaks.add(cocl);	
				updateKnowledgeGraph(cocl);
				
				///
				/*
				 * object creation and cascades with variable parameter
				 * PayInfo p = new PayInfo(w); 
				 * PayInfo p2 = new PayInfo(new WorkInfo(p));
				 */
				String parentId = Client.getDisplay().astview.getParentIdentifier(id);
				String parentLabel = Client.getDisplay().astview.getVertexLabel(parentId);
				if(parentLabel.contains("ObjectCreationExpr")) {
					ClassCascadingObjectCreationLeak cascadingLeak = new ClassCascadingObjectCreationLeak(id, CodeVisitor.className, leakline);
					while(parentLabel.equals("ObjectCreationExpr")) {
						String containingObjectType = createdObjectTypes.get(parentId);
						CascadeObjectCreation cascade = new CascadeObjectCreation(id,parentId,null,accessVariableType, null,containingObjectType, leakline);
						cascadingLeak.addObjectCreation(cascade);
						
						parentId = Client.getDisplay().astview.getParentIdentifier(parentId);//get grandparent
						parentLabel = Client.getDisplay().astview.getVertexLabel(parentId);
						
					}
						
					ArrayList<CascadeObjectCreationLeak> cascadeLeaks = cascadingLeak.extractLeaks();
					for(CascadeObjectCreationLeak leak: cascadeLeaks) {
						leak.setCodeSource(sourceCode);
						leaks.add(leak);	
						updateKnowledgeGraph(leak);
					}
				}
				///
			}
		}
		catch (UnsolvedSymbolException e) {
			Client.getDisplay().updateLogPage("nUnsolvable: "+n.toString(), true);
		}catch(RuntimeException e) {
//			Client.getDisplay().updateLogPage("nrUnsolvable: "+e.getMessage(), true);
		}
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(NormalAnnotationExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "NormalAnnotationExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(NullLiteralExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "NullLiteralExpr:"+n.toString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ObjectCreationExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		String sourceCode = n.toString();

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);
		
		Object child = Client.getDisplay().astview.createVertex(id, "ObjectCreationExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);				
		try {
			Type type = JavaParserFacade.get( ts ).getType( n );
			String reference_tx = type.describe();			
			if(!(type.isPrimitive()||
					reference_tx.startsWith("java.") ||
					reference_tx.startsWith("javax.") ||
					reference_tx.startsWith("org.omg.") ||
					reference_tx.startsWith("org.w3c.dom.") ||
					reference_tx.startsWith("org.xml.sax."))) {
				
				/*
				 * Object creation leak (without parameters)
				 * Example:
				 * 	WorkInfo w = new WorkInfo();
				 */
				String createdObjectType = JavaParserFacade.get(ts).solve(n).getCorrespondingDeclaration().declaringType().getName();
				String leakline = n.getRange().get().toString();
				ClassObjectCreationLeak cocl = new ClassObjectCreationLeak(id,null,createdObjectType, CodeVisitor.className, leakline);
				cocl.setCodeSource(sourceCode);
				createdObjectTypes.put(id, createdObjectType);
				
				leaks.add(cocl);	
				updateKnowledgeGraph(cocl);
				
				/*
				 * Object creation cascade leak (without parameters)
				 * Example:
				 * PayInfo p = new PayInfo(new WorkInfo());

				 */

				String parentId = Client.getDisplay().astview.getParentIdentifier(id);
				String parentLabel = Client.getDisplay().astview.getVertexLabel(parentId);
				if(parentLabel.contains("ObjectCreationExpr")) {
					ClassCascadingObjectCreationLeak cascadingLeak = new ClassCascadingObjectCreationLeak(id, CodeVisitor.className, leakline);
					while(parentLabel.equals("ObjectCreationExpr")) {
						String containingObjectType = createdObjectTypes.get(parentId);
						CascadeObjectCreation cascade = new CascadeObjectCreation(id,parentId,null,createdObjectType, null,containingObjectType, leakline);
						cascadingLeak.addObjectCreation(cascade);
						
						parentId = Client.getDisplay().astview.getParentIdentifier(parentId);//get grandparent
						parentLabel = Client.getDisplay().astview.getVertexLabel(parentId);
						
					}
						
					ArrayList<CascadeObjectCreationLeak> cascadeLeaks = cascadingLeak.extractLeaks();
					for(CascadeObjectCreationLeak leak: cascadeLeaks) {
						leak.setCodeSource(sourceCode);
						leaks.add(leak);		
						updateKnowledgeGraph(leak);
					}
				}
				
			}
		}
		catch (UnsolvedSymbolException e) {
			Client.getDisplay().updateLogPage("mUnsolvable: "+n.toString(), true);
		}catch(RuntimeException e) {
			Client.getDisplay().updateLogPage("mrUnsolvable: "+e.getMessage(), true);
		}
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(Parameter n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);
		
		Object child = Client.getDisplay().astview.createVertex(id, "Parameter");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		super.visit(n, id);
	}
	
	
	@Override
	public void visit(PrimitiveType n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "PrimitiveType:"+n.asString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(Name n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "Name:"+n.asString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);		
	}
	
	@Override
	public void visit(SimpleName n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);
		
		Object child = Client.getDisplay().astview.createVertex(id, "SimpleName:"+n.asString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);		
	}
	
	@Override
	public void visit(ArrayType n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ArrayType");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ArrayCreationLevel n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ArrayCreationLevel");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(IntersectionType n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);
		
		Object child = Client.getDisplay().astview.createVertex(id, "IntersectionType");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(UnionType n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);
		
		Object child = Client.getDisplay().astview.createVertex(id, "UnionType");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ReturnStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ReturnStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(SingleMemberAnnotationExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);
		
		Object child = Client.getDisplay().astview.createVertex(id, "SingleMemberAnnotationExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(StringLiteralExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);
		
		Object child = Client.getDisplay().astview.createVertex(id, "StringLiteralExpr:"+n.toString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		

		super.visit(n, id);
	}

	@Override
	public void visit(SuperExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);
		
		Object child = Client.getDisplay().astview.createVertex(id, "SuperExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		

		super.visit(n, id);
	}
	
	@Override
	public void visit(SwitchEntryStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "SwitchEntryStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(SwitchStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "SwitchStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(SynchronizedStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "SynchronizedStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ThisExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ThisExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ThrowStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ThrowStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(TryStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "TryStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(LocalClassDeclarationStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);


		Object child = Client.getDisplay().astview.createVertex(id, "LocalClassDeclarationStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);
		
		super.visit(n, id);
	}
	
	@Override
	public void visit(TypeParameter n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		Object child = Client.getDisplay().astview.createVertex(id, "TypeParameter:"+n.getNameAsString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(PackageDeclaration n, String arg) {		
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "PackageDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(UnaryExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "UnaryExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);		
	}
	
	@Override
	public void visit(UnknownType n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "UnknownType");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(VariableDeclarationExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "VariableDeclarationExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(VariableDeclarator n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "VariableDeclarator");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(VoidType n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "VoidType");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(WhileStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "WhileStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(WildcardType n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "WildcardType");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
		
	}

	@Override
	public void visit(LambdaExpr n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "LambdaExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
		
	}

	@Override
	public void visit(MethodReferenceExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "MethodReferenceExpr");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
		
	}

	@Override
	public void visit(TypeExpr n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "TypeExpr:"+n.toString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(ImportDeclaration n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ImportDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(ModuleDeclaration n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ModuleDeclaration");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(ModuleRequiresStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ModuleRequiresStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(ModuleExportsStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ModuleExportsStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(ModuleProvidesStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ModuleProvidesStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(ModuleUsesStmt n, String arg) {
		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ModuleUsesStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(ModuleOpensStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ModuleOpensStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}

	@Override
	public void visit(UnparsableStmt n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;

		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "UnparsableStmt");
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);		
		super.visit(n, id);
	}
	
	@Override
	public void visit(ReceiverParameter n, String arg) {

		String id = "ast"+counter;
		counter = counter+1;
		
		addVertexCodeAttribution(id,n.toString());
		addVertexChild(arg, id);

		Object child = Client.getDisplay().astview.createVertex(id, "ReceiverParameter:"+n.getNameAsString());
		Object parent = Client.getDisplay().astview.getVertex(arg);
		
		Client.getDisplay().astview.addEdge(parent, child, arg, id);
		super.visit(n, id);

	}
	

	public static void addVertexCodeAttribution(String vertexIdentifier, String codeattribution) {
		vertexCodeAttributions.put(vertexIdentifier, codeattribution);
	}
	
	public static String getVertexCodeAttribution(String vertexIdentifier) {
		return vertexCodeAttributions.get(vertexIdentifier);
	}
	
	public static void addVertexParent(String vertexIdentifier, String parentIdentifier) {
		vertexParent.put(vertexIdentifier, parentIdentifier);
	}
	
	public static String getVertexParent(String vertexIdentifier) {
		return vertexParent.get(vertexIdentifier);
	}
	public static void addVertexChild(String vertexIdentifier, String childIdentifier) {
		ArrayList<String> children = vertextChildren.get(vertexIdentifier);
		if(children == null) {
			children = new ArrayList<String>();
		}
		children.add(childIdentifier);
		vertextChildren.put(vertexIdentifier, children);
	}	
	
	public static ArrayList<String> getVertexChildren(String vertexIdentifier){
		return vertextChildren.get(vertexIdentifier);
	}
	
	
	public static CombinedTypeSolver setupTypeSolver() {
        CombinedTypeSolver combinedTypeSolver = new CombinedTypeSolver();
        
        combinedTypeSolver.add(new ReflectionTypeSolver());
        
        if(TypeInputSourcesDialog.jarDependencies !=null) {
            for(File f:TypeInputSourcesDialog.jarDependencies) {
            	    try {
            	    		combinedTypeSolver.add(new JarTypeSolver(f.getPath()));
				} 
            	    	catch (IOException e) {
					e.printStackTrace();
				}
            }

        }
        if(TypeInputSourcesDialog.srcDependencies !=null) {
            for(File f:TypeInputSourcesDialog.srcDependencies) {
	            	if(f.exists()) {
		    			combinedTypeSolver.add(new JavaParserTypeSolver(f));
	            	}
            }
        }
        return combinedTypeSolver;
	}
	
	public synchronized void updateKnowledgeGraph(Leak leak) {
 		Client.getDisplay().kgview.addLeaks(leak);

	}
}
