/**
 * Copyright 2012 Cheng Wei, Robert Taylor
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions
 * and limitations under the License.
 */
package org.robobinding

import java.io.File;

import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 *
 * @goal validate-xml
 * @phase validate
 * 
 * @since 1.0
 * @version $Revision: 1.0 $
 * @author Robert Taylor
 */
class GroovyBindingAttributeValidatorMojo extends AbstractMojo
{
	/**
	 * @parameter expression="${basedir}"
	 * @required
	 */
	def baseFolder;
	def resFolder
	
	public void execute() throws MojoExecutionException, MojoFailureException
	{
		getLog().info("Validating binding attributes...")
		
		inEachLayoutFolder {
			inEachXmlFileWithBindings(it) {
				
				
			}
		}
		
		//4. Parse the xml into data structure - use regexps for fast processing
		
		//5. For each binding attribute declared, check the corresponding view agains the candidate providers (BindingAttributeProviderResolver.getCandidateProviders()
		
		//6. If attribute is not resolved, add it to the list
	}

	
	def inEachLayoutFolder (Closure c) {
		getResFolder().eachDirMatch(~/[layout].*/) {
			c.call(it)
		}
	}
	
	def inEachXmlFile(folder, Closure c) {
		folder.eachFileMatch(~/.*[.xml]/) {
			c.call(it)
		}
	}
	
	def inEachXmlFileWithBindings(folder, Closure c) {
		inEachXmlFile(it) {
			if (getRoboBindingNamespaceDeclaration(it.text)) {
				c.call(it)
			}
		}
	}
	
	def getRoboBindingNamespaceDeclaration(xml) {
		def rootNode = new XmlSlurper().parseText(xml)
		
		def xmlClass = rootNode.getClass()
		def gpathClass = xmlClass.getSuperclass()
		def namespaceTagHints = gpathClass.getDeclaredField("namespaceTagHints")
		namespaceTagHints.setAccessible(true)
		
		def namespaceDeclarations = namespaceTagHints.get(rootNode)
		
		for (String name : namespaceDeclarations.keySet()) {
			if (namespaceDeclarations.get(name) == 'http://robobinding.org/android') {
				return name
			}
		}
	}
	
	def getResFolder() {
		if (resFolder == null)
			resFolder = new File(baseFolder, "res")
			
		resFolder	
	}
}