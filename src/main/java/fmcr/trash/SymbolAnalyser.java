package fmcr.trash;

import java.io.File;
import java.io.IOException;
import java.util.List;

import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import com.github.javaparser.symbolsolver.javaparser.Navigator;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.declarations.MethodDeclaration;
import com.github.javaparser.symbolsolver.model.typesystem.Type;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import fmcr.display.TypeInputSourcesDialog;
import fmcr.main.Client;
import fmcr.visitors.CodeVisitor;

public class SymbolAnalyser {
	private static CombinedTypeSolver combinedTypeSolver;

	public static void typeFinder() {
		combinedTypeSolver = SymbolAnalyser.setupTypeSolver();
		
		List<ObjectCreationExpr> ocexp = Navigator.findAllNodesOfGivenClass(Client.getCompilationUnit(), ObjectCreationExpr.class);
		Client.getDisplay().updateLogPage("ObjectCreationLeaks:", false);				

		ocexp.forEach(oc ->{
			try {
				String reference_t = JavaParserFacade.get(combinedTypeSolver).solve(oc).getCorrespondingDeclaration().declaringType().getClassName();
				
				String k_construct = "K<sub>"+CodeVisitor.className+"</sub>("+ reference_t +")";
				Client.getDisplay().updateLogPage(k_construct, false);				
			}
			catch (UnsolvedSymbolException e) {
				Client.getDisplay().updateLogPage("mUnsolvable: "+oc.toString(), true);
			}catch(RuntimeException e) {
				Client.getDisplay().updateLogPage("mUnsolvable: "+e.getMessage(), true);
			}
		});

		Client.getDisplay().updateLogPage("MethodCallLeaks(L1):", false);				
		List<MethodCallExpr> methodCalls = Navigator.findAllNodesOfGivenClass(Client.getCompilationUnit(), MethodCallExpr.class);
		methodCalls.forEach(mc ->{
			try {

//				String reference_t = JavaParserFacade.get(combinedTypeSolver).solveMethodAsUsage(mc).declaringType().getClassName();
//				String reference_t = JavaParserFacade.get(combinedTypeSolver).getType(mc).describe();

				String reference_t = JavaParserFacade.get(combinedTypeSolver).solve(mc).getCorrespondingDeclaration().declaringType().getClassName();
				String return_t = JavaParserFacade.get(combinedTypeSolver).solve(mc).getCorrespondingDeclaration().getReturnType().describe();
				
				if(!return_t.equals("void")) {
					String k_construct = "K<sub>"+reference_t+"</sub>("+ return_t +")";
					Client.getDisplay().updateLogPage(k_construct, false);
				}				
			}
			catch (UnsolvedSymbolException e) {
				Client.getDisplay().updateLogPage("mUnsolvable: "+mc.toString(), true);
			}catch(RuntimeException e) {
				Client.getDisplay().updateLogPage("mrUnsolvable: "+e.getMessage(), true);
			}
		});
		
		Client.getDisplay().updateLogPage("NameExprLeaks:", false);				
		List<NameExpr> nameexprs = Navigator.findAllNodesOfGivenClass(Client.getCompilationUnit(), NameExpr.class);
		nameexprs.forEach(ne ->{
			try {
				Type t = JavaParserFacade.get(combinedTypeSolver).solve(ne).getCorrespondingDeclaration().getType();
				String desc = t.describe();
				String t_name = "";
				if(desc.contains(".")) {
					String [] t_name1 = desc.split("\\.");
					t_name = t_name1[t_name1.length-1];	
				}
				else {
					t_name = desc;
				}
				String k_construct = "K<sub>"+CodeVisitor.className+"</sub>("+ t_name +")";
				Client.getDisplay().updateLogPage(k_construct, false);

			}
			catch (UnsolvedSymbolException e) {
				Client.getDisplay().updateLogPage("nUnsolvable: "+ne.toString(), true);
			}catch(RuntimeException e) {
//				Client.getDisplay().updateLogPage("nrUnsolvable: "+e.getMessage(), true);
			}
		});	
		
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

}
