package com.railinc.r2dq.correspondence;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.io.File;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.velocity.app.VelocityEngine;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.base.Joiner;
import com.railinc.r2dq.configuration.ConfigurationServiceMapSupplier;
import com.railinc.r2dq.configuration.R2DQConfigurationService;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"/spring-r2dq-velocity.xml"})
public class BasicTemplateTest {
	
	@Rule
	public TestName testName = new TestName();
	
	@Autowired
	VelocityEngine engine;

	
	Template2Correspondence xformer;

	protected R2DQConfigurationService configServiceMock;
	
	@Before
	public void setup() {
		configServiceMock = mock(R2DQConfigurationService.class);
		xformer = new Template2Correspondence();
		xformer.setEngine(engine);
		xformer.setParameterSupplier(new ConfigurationServiceMapSupplier(configServiceMock));
	}

	/**
	 * correspondence.output.dir is passed in by maven so that we push our output
	 * into the target/classes directory (or wherever specified).  this allows jenkins
	 * to 'find' the outputted html files to archive/expose them.
	 * @return
	 */
	public File correspondenceDirectory() {
		String value = System.getProperty("correspondence.output.dir",System.getProperty("java.io.tmpdir"));
		File dir = new File(value);
		if (!dir.exists()) {
			boolean mkdirs = dir.mkdirs();
			if (!mkdirs) {
				throw new RuntimeException("Unable to make directory " + dir.getPath());
			}
		}
		return dir;
	}
	public Correspondence process(CorrespondenceTemplate template) {
		Correspondence c = xformer.apply(template);
		
		File tmp = correspondenceDirectory();
		
		try {
			File file = new File(tmp, getClass().getSimpleName() + "_"+ testName.getMethodName() + ".txt");
			FileWriter w = new FileWriter(file);
			w.write(c.getTextPlain());
			w.close();
			System.out.println("Wrote output to " + file.getAbsolutePath());

	
	
			file = new File(tmp,getClass().getSimpleName() + "_"+ testName.getMethodName() + ".subject.txt");
			w = new FileWriter(file);
			w.write(c.getSubject());
			w.close();
			System.out.println("Wrote output to " + file.getAbsolutePath());

			file = new File(tmp,getClass().getSimpleName() + "_"+ testName.getMethodName() + ".html");
			w = new FileWriter(file);
			w.write(c.getTextHtml());
			w.close();
			System.out.println("Wrote output to " + file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return c;
	}



	protected Correspondence assertIsComplete(Correspondence c) {
		assertHasSubject(c);

		assertTextIsProcessed(c);
		
		assertHasTextPlain(c);
		assertHasTextPlainHeader(c);
		assertHasTextPlainFooter(c);

		assertHasHtml(c);
		assertHasHtmlHeader(c);
		assertHasHtmlFooter(c);

		return c;
	}

	
	private void assertHasTextPlainHeader(Correspondence c) {
//		assertTrue("has text header", c.getTextPlain()"");
	}


	private void assertHasHtmlHeader(Correspondence c) {
		assertTrue("has html header", c.getTextHtml().contains("<img align=\"absmiddle\" src=\"https://www.railinc.com/images/logo.png\"/>"));
	}


	private void assertHasHtmlFooter(Correspondence c) {
		assertTrue("has html footer", c.getTextHtml().contains("If you believe that this email was sent to you in error, or have any questions/concerns, please contact :") 
				&& c.getTextHtml().contains("Railinc Customer Support Center by phone at 1-877-RAILINC (1-877-724-5462) or at"));
	}


	private void assertHasTextPlainFooter(Correspondence c) {
		assertTrue("has text plain footer", c.getTextPlain().contains("If you believe that this email was sent to you in error, or have any questions/concerns, please contact Railinc Customer Support Center by phone at 1-877-RAILINC (1-877-724-5462)"));
	}


	private void assertTextIsProcessed(Correspondence c) {
		if (isNotBlank(c.getTextHtml())) {
			assertFalse(shouldntContainDirectives("html", c.getTextHtml()), c.getTextHtml().contains("$") || c.getTextHtml().contains("{"));
		}
		assertFalse(shouldntContainDirectives("text", c.getTextPlain()), c.getTextPlain().contains("$") || c.getTextPlain().contains("{"));
		assertFalse(shouldntContainDirectives("subject", c.getSubject()), c.getSubject().contains("$") || c.getSubject().contains("{"));
	}


	private String shouldntContainDirectives(String contentType, String subject) {
		if (isBlank(subject)) {
			return "text is blank";
		}
		Pattern p = Pattern.compile("(\\{[^\\}]+})");
		Matcher matcher = p.matcher(subject);
		List<String> l = newArrayList();
		
		while (matcher.find()) {
			l.add(matcher.group(1));
		}
		return "Found:" + Joiner.on(",").join(l) + " in " + contentType;
	}


	private void assertHasTextPlain(Correspondence c) {
		assertTrue(isNotBlank(c.getTextPlain()));
	}



	private void assertHasSubject(Correspondence c) {
		assertTrue("Should have a subject", isNotBlank(c.getSubject()));
	}



	private void assertHasHtml(Correspondence c) {
		assertTrue("Should have HTML", isNotBlank(c.getTextHtml()));
	}


	public final void assertHasText(Correspondence c, String ... txt) {
		for (String t : txt) {
			assertTrue(format("text/plain should have '%s'",t) , c.getTextPlain().contains(t));
			if (isNotBlank(c.getTextHtml())) {
				assertTrue("text/html has " + t, c.getTextHtml().contains(t));
			}
		}
		
	}
	protected SimpleContact adminContact() {
		return new SimpleContact("The Admin", "the@admin.com");
	}

}
