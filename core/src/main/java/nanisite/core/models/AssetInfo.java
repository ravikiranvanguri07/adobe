package nanisite.core.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;
import javax.jcr.Node;
import javax.jcr.Property;
import javax.jcr.RepositoryException;
import javax.jcr.ValueFormatException;
import javax.jcr.lock.LockException;
import javax.jcr.nodetype.ConstraintViolationException;
import javax.jcr.version.VersionException;

import org.apache.sling.api.resource.PersistenceException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.slf4j.LoggerFactory;

import com.day.cq.dam.api.Asset;

import nanisite.core.pojo.AssetMetaDataPojo;
import nanisite.core.utility.ResourceResolverForAccess;

@Model(adaptables = { Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class AssetInfo {

	private static final org.slf4j.Logger log = LoggerFactory.getLogger(AssetInfo.class);

	@Inject
	public String assetPathField;

	@Inject
	ResourceResolverForAccess resourceResolverForAccess;

	public List<AssetMetaDataPojo> getAssetData() {
		List<AssetMetaDataPojo> assetMetaDataPojoList = new ArrayList();
		ResourceResolver resourceResolver = resourceResolverForAccess.getResourceResolverMethod();
		if (resourceResolver != null) {
			log.info("Accessed Resource Resolver: {} ",resourceResolver );
			Resource resource = resourceResolver.getResource(assetPathField);
			if (resource != null && resource.hasChildren()) {
				java.lang.Iterable<Resource> assetChildren = resource.getChildren();
				Iterator<Resource> iterator = assetChildren.iterator();
				log.info("Asset Children listed: {} ",assetChildren.toString() );
				while (iterator.hasNext()) {
					Resource assetResource = iterator.next();
					log.info("assetResource: {} ",assetResource.getPath() );
					if (assetResource != null) {
						Resource metadataResource = assetResource.getResourceResolver()
								.getResource(assetResource.getPath() + "/jcr:content/metadata");
						if(metadataResource!=null) {
						log.info("MetadataResource: {} ",metadataResource.getPath() );
						addPropertyToNode(resourceResolver, metadataResource);
						log.info("Executing the node property: {} {} ",resourceResolver, metadataResource.getPath() );
						Asset asset = assetResource.adaptTo(Asset.class);
						ValueMap valueMap = metadataResource.getValueMap();
						AssetMetaDataPojo assetMetadataDetails = new AssetMetaDataPojo();
						assetMetadataDetails.setName(asset.getName());
						assetMetadataDetails.setPath(asset.getPath());
						assetMetadataDetails.setSize(valueMap.get("dam:size", "10"));
						assetMetadataDetails.setMimeType(valueMap.get("dam:MIMEtype", "image format"));
						assetMetaDataPojoList.add(assetMetadataDetails);
					}
					}
				}
			}
		}
		return assetMetaDataPojoList;
	}

	public void addPropertyToNode(ResourceResolver resourceResolver, Resource metadataResource) {
		Node node = metadataResource.adaptTo(Node.class);
		try {
			Property nodeProperty = node.setProperty("Nani", "Suresh");
			resourceResolver.commit();
		} catch (ValueFormatException e) {
			log.info("{} {}", e);
		} catch (VersionException e) {
			log.info("{} {}", e);
		} catch (LockException e) {
			log.info("{} {}", e);
		} catch (ConstraintViolationException e) {
			log.info("{} {}", e);
		} catch (RepositoryException e) {
			log.info("{} {}", e);
		} catch (PersistenceException e) {
			log.info("{} {}", e);
		}
	}
}
