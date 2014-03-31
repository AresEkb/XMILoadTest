package xmiLoadTest;

import java.io.File;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.plugin.EcorePlugin;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Factory.Registry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.ocl.examples.domain.utilities.ProjectMap;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;
import org.eclipse.uml2.uml.Stereotype;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UML212UMLResource;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.eclipse.uml2.uml.resource.XMI2UMLResource;
import org.eclipse.uml2.uml.resources.util.UMLResourcesUtil;

public class Main {

    public static void main(String[] args) {
        final String input = "model/Test.uml";

        ResourceSet rs = new ResourceSetImpl();
        UMLResourcesUtil.init(rs);
        rs.setURIConverter(new CustomURIConverter());
        rs.getPackageRegistry().put(
                "http://www.eclipse.org/uml2/2.1.0/UML", UMLPackage.eINSTANCE);
//        rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
//                UMLResource.FILE_EXTENSION, UMLResource.Factory.INSTANCE);
//        rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
//                XMI2UMLResource.FILE_EXTENSION, XMI2UMLResource.Factory.INSTANCE);

        // The following code will initialize EcorePlugin.getPlatformResourceMap()
//        org.eclipse.ocl.examples.pivot.OCL.initialize(rs);
//        org.eclipse.ocl.examples.pivot.uml.UML2Pivot.initialize(rs);
//        org.eclipse.ocl.examples.pivot.model.OCLstdlib.install();
//        org.eclipse.ocl.examples.pivot.delegate.OCLDelegateDomain.initialize(rs);
//        org.eclipse.ocl.examples.xtext.completeocl.CompleteOCLStandaloneSetup.doSetup();
//        org.eclipse.ocl.examples.xtext.oclinecore.OCLinEcoreStandaloneSetup.doSetup();
//        org.eclipse.ocl.examples.xtext.oclstdlib.OCLstdlibStandaloneSetup.doSetup();
        org.eclipse.ocl.examples.domain.utilities.StandaloneProjectMap.getAdapter(rs);

        // The following URI isn't resolved. And it causes an exception later.
        System.out.println("UML21_2_UML.ecore2xml resolved to " + EcorePlugin.resolvePlatformResourcePath(
                "platform:/plugin/org.eclipse.uml2.uml/model/UML21_2_UML.ecore2xml"));

        System.out.println("\n--- Platform Resource Map:");
        printMap(EcorePlugin.getPlatformResourceMap());
        System.out.println("\n--- Global Resource Factory Registry:");
        printResourceFactoryRegistry(Registry.INSTANCE);
        System.out.println("\n--- Local Resource Factory Registry:");
        printResourceFactoryRegistry(rs.getResourceFactoryRegistry());
        System.out.println("\n--- Package Registry:");
        printMap(rs.getPackageRegistry());

        Resource resource = rs.getResource(createFileURI(input), true);
        System.out.println("Root: " + resource.getContents().get(0).getClass());
        System.out.println("Count: " + resource.getContents().size());
        Model uml = (Model)EcoreUtil.getObjectByType(
                resource.getContents(), UMLPackage.eINSTANCE.getModel());
        for (Profile profile : uml.getAllAppliedProfiles()) {
            System.out.println(profile);
//            for (Element el : profile.allOwnedElements()) {
//                System.out.println(el);
//            }
        }
        for (Stereotype stereotype : uml.getAppliedStereotypes()) {
            System.out.println(stereotype);
        }
    }

    private static void printResourceFactoryRegistry(Registry registry)
    {
        System.out.println("ExtensionToFactoryMap:");
        printMap(registry.getExtensionToFactoryMap());
        System.out.println("\nContentTypeToFactoryMap:");
        printMap(registry.getContentTypeToFactoryMap());
        System.out.println("\nProtocolToFactoryMap:");
        printMap(registry.getProtocolToFactoryMap());
        System.out.println();
    }

    private static URI createFileURI(String relativePath)
    {
        return URI.createFileURI(new File(relativePath).getAbsolutePath());
    }
    
    private static void printMap(Map<String, ?> map)
    {
        for (String key : map.keySet()) {
            System.out.println(key + " : " + map.get(key));
        }
    }
}
