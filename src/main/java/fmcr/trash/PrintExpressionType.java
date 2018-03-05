/*
 * Copyright 2016 Federico Tomassetti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fmcr.trash;

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
import com.github.javaparser.symbolsolver.resolution.typesolvers.JavaParserTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;

import fmcr.main.Client;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class PrintExpressionType {


    static class TypeCalculatorVisitor extends VoidVisitorAdapter<JavaParserFacade> {
        @Override
        public void visit(ReturnStmt n, JavaParserFacade javaParserFacade) {
            super.visit(n, javaParserFacade);
//            System.out.println(n.getExpr().toString() + " has type " + javaParserFacade.getType(n.getExpr()));
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

    public static void main(String[] args) throws FileNotFoundException, ParseException {
        
        JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Select Source File ");
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);//.FILES_AND_DIRECTORIES);
		chooser.setAcceptAllFileFilterUsed(false);
		FileNameExtensionFilter filter = new FileNameExtensionFilter(".java", "java", "java");
		chooser.setFileFilter(filter);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			BufferedReader buff = null;
			String[] s = chooser.getSelectedFile().toString().split(Pattern.quote("."));
			if(!s[s.length-1].equals("java")) {
				return;
			}

			try {	
				String dirpath = chooser.getSelectedFile().getParent();
		        TypeSolver typeSolver = new CombinedTypeSolver(new ReflectionTypeSolver(), new JavaParserTypeSolver(new File(dirpath)));

				buff = new BufferedReader(new FileReader(chooser.getSelectedFile()));
				
				CompilationUnit agendaCu = JavaParser.parse(buff);

		        agendaCu.accept(new TypeCalculatorVisitor(), JavaParserFacade.get(typeSolver));
			}
			catch (IOException e) {
				System.err.print(e);
			} 			
		}  
        
        
        
        
        
        
        

    }

}
