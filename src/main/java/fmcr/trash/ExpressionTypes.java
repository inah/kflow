package fmcr.trash;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.github.javaparser.resolution.types.ResolvedReferenceType;
import com.github.javaparser.symbolsolver.javaparsermodel.JavaParserFacade;
import com.github.javaparser.symbolsolver.model.resolution.TypeSolver;
import com.github.javaparser.symbolsolver.model.typesystem.ReferenceType;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JarTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import fmcr.display.TypeInputSourcesDialog;

public class ExpressionTypes {
	static class TypeCalculatorVisitor extends VoidVisitorAdapter<JavaParserFacade> {
        @Override
        public void visit(ReturnStmt n, JavaParserFacade javaParserFacade) {
            super.visit(n, javaParserFacade);
//            System.out.println(n.getExpression().toString() + " has type " + javaParserFacade.getType(n.getExpression()));
        }

        @Override
        public void visit(MethodCallExpr n, JavaParserFacade javaParserFacade) {
            super.visit(n, javaParserFacade);
            System.out.println(n.toString() + " has type " + javaParserFacade.getType(n).describe());
            if (javaParserFacade.getType(n).isReferenceType()) {
                for (ReferenceType ancestor : javaParserFacade.getType(n).asReferenceType().getAllAncestors()) {
                    System.out.println("Ancestor " + ancestor.describe());
                }
            }
        }
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
	
	 public static void main(String[] args) throws FileNotFoundException, ParseException {
	        TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(new File("/Users/inah/Desktop/")));

	        CompilationUnit agendaCu = JavaParser.parse(new FileInputStream(new File("/Users/inah/Desktop/VehicleControlSystem/src/oose/vcs/Car.java")));

	        agendaCu.accept(new TypeCalculatorVisitor(), JavaParserFacade.get(typeSolver));

	    }
}
