package org.canova.nlp.tokenization.tokenizerfactory;

import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.fit.factory.AnalysisEngineFactory;
import org.apache.uima.resource.ResourceInitializationException;
import org.canova.nlp.annotator.SentenceAnnotator;
import org.canova.nlp.annotator.TokenizerAnnotator;
import org.canova.nlp.tokenization.tokenizer.TokenPreProcess;
import org.canova.nlp.tokenization.tokenizer.Tokenizer;
import org.canova.nlp.tokenization.tokenizer.UimaTokenizer;
import org.canova.nlp.uima.UimaResource;

import java.io.InputStream;


/**
 * Uses a uima {@link AnalysisEngine} to 
 * tokenize text.
 *
 *
 * @author Adam Gibson
 *
 */
public class UimaTokenizerFactory implements TokenizerFactory {


	private UimaResource uimaResource;
	private boolean checkForLabel;
	private static AnalysisEngine defaultAnalysisEngine;
    private TokenPreProcess preProcess;

	public UimaTokenizerFactory() throws ResourceInitializationException {
		this(defaultAnalysisEngine(),true);
	}


	public UimaTokenizerFactory(UimaResource resource) {
		this(resource,true);
	}


	public UimaTokenizerFactory(AnalysisEngine tokenizer) {
		this(tokenizer,true);
	}



	public UimaTokenizerFactory(UimaResource resource,boolean checkForLabel) {
		this.uimaResource = resource;
		this.checkForLabel = checkForLabel;
	}

	public UimaTokenizerFactory(boolean checkForLabel) throws ResourceInitializationException {
		this(defaultAnalysisEngine(),checkForLabel);
	}



	public UimaTokenizerFactory(AnalysisEngine tokenizer,boolean checkForLabel) {
		super();
		this.checkForLabel = checkForLabel;
		try {
			this.uimaResource = new UimaResource(tokenizer);


		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}



	@Override
	public Tokenizer create(String toTokenize) {
		if(toTokenize == null || toTokenize.isEmpty())
			throw new IllegalArgumentException("Unable to proceed; on sentence to tokenize");
		Tokenizer ret =  new UimaTokenizer(toTokenize,uimaResource,checkForLabel);
        ret.setTokenPreProcessor(preProcess);
        return ret;
	}


	public UimaResource getUimaResource() {
		return uimaResource;
	}


	/**
	 * Creates a tokenization,/stemming pipeline
	 * @return a tokenization/stemming pipeline
	 */
	public static AnalysisEngine defaultAnalysisEngine()  {
		try {
			if(defaultAnalysisEngine == null)

				defaultAnalysisEngine =  AnalysisEngineFactory.createEngine(
						AnalysisEngineFactory.createEngineDescription(
								SentenceAnnotator.getDescription(),
								TokenizerAnnotator.getDescription()));

			return defaultAnalysisEngine;
		}catch(Exception e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public Tokenizer create(InputStream toTokenize) {
		throw new UnsupportedOperationException();
	}

    @Override
    public void setTokenPreProcessor(TokenPreProcess preProcessor) {
        this.preProcess = preProcessor;
    }


}
