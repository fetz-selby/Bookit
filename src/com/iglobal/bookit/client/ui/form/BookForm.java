package com.iglobal.bookit.client.ui.form;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.SpanElement;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.EventListener;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import com.iglobal.bookit.client.GlobalResource;
import com.iglobal.bookit.client.constants.EntityEnum;
import com.iglobal.bookit.client.events.RPCCompleteHandler;
import com.iglobal.bookit.client.ui.components.ComplexSortableList;
import com.iglobal.bookit.client.ui.components.ComplexSortableList.ColumnObject;
import com.iglobal.bookit.client.ui.components.ComplexSortableList.ComplexSortableListEventHandler;
import com.iglobal.bookit.client.ui.components.CustomConfirmationPanel;
import com.iglobal.bookit.client.ui.components.CustomConfirmationPanel.CustomConfirmationPanelEventHandler;
import com.iglobal.bookit.client.ui.components.CustomSuggestBox;
import com.iglobal.bookit.client.ui.components.CustomSuggestBox.CustomSuggestBoxEventHandler;
import com.iglobal.bookit.client.ui.components.SortableList;
import com.iglobal.bookit.client.ui.components.SortableList.SortableListEventHandler;
import com.iglobal.bookit.client.ui.components.object.SortableObject;
import com.iglobal.bookit.client.user.widget.ScrollableFlowPanel;
import com.iglobal.bookit.client.utils.ErrorIndicator;
import com.iglobal.bookit.client.utils.Utils;
import com.iglobal.bookit.shared.BookRowObject;

public class BookForm extends Composite {

	//private String[] blacklistKeywords = {"id", "created_ts", "status", "ANALYZE", "ACCESSIBLE", "ADD", "ALL", "ALTER", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE", "BETWEEN","BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL", "CASCADE", "CASE", "CHANGE",	"CHAR", "CHARACTER", "CHECK", "COLLATE", "COLUMN", "CONDITION", "CONSTRAINT", "CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND", "DAY_MINUTE", "DAY_SECOND","DEC", "DECIMAL", "DECLARE", "DEFAULT", "DELAYED", "DELETE", "DESC",	"DESCRIBE", "DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE", "DROP", "DUAL", "EACH", "ELSE", "ELSEIF", "ENCLOSED", "ESCAPED", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH", "FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM", "FULLTEXT", "GENERAL[a]", "GRANT", "GROUP", "HAVING", "HIGH_PRIORITY", "HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF", "IGNORE", "IGNORE_SERVER_IDS[b]", "IN", "INDEX", "INFILE", "INNER", "INOUT",	"INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3", "INT4", "INT8", "INTEGER", "INTERVAL", "INTO", "IS", "ITERATE", "JOIN", "KEY", "KEYS", "KILL", "LEADING", "LEAVE", "LEFT", "LIKE", "LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME", "LOCALTIMESTAMP",	"LOCK", "	LONG", "LONGBLOB", "LONGTEXT", "LOOP", "LOW_PRIORITY", "MASTER_HEARTBEAT_PERIOD[c]", "MASTER_SSL_VERIFY_SERVER_CERT", "MATCH", "MAXVALUE", "MEDIUMBLOB", "MEDIUMINT", "MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND", "MINUTE_SECOND", "MOD","MODIFIES",	"NATURAL", "NOT", "NO_WRITE_TO_BINLOG","NULL", "NUMERIC", "ON", "OPTIMIZE", "OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER", "OUTFILE", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE", "RANGE", "READ", "READS", "READ_WRITE", "REAL", "REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT", "REPLACE", "REQUIRE", "RESIGNAL", "RESTRICT", "RETURN", "REVOKE", "RIGHT", "RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT", "SENSITIVE", "SEPARATOR", "SET", "SHOW", "SIGNAL", "SLOW[d]", "SMALLINT", "SPATIAL", "SPECIFIC	SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT", "SSL", "STARTING", "STRAIGHT_JOIN", "TABLE", "TERMINATED", "THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING", "TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK", "UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE", "UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR", "VARCHARACTER", "VARYING", "WHEN", "WHERE", "WHILE", "WITH", "WRITE", "XOR", "YEAR_MONTH", "ZEROFILL"};
	private String[] forbiddenChar = {"(", ")", "%", "@", "#", "$", "^", "&", "*", "!", "{", "}", "+", "=", "'", "<", ">", "/", "?", "`", "~", "/", "\\", ":", ";", ",", ".", "[", "]", "|"};
	private ArrayList<String> forbiddenCharList;
	private String status = "A";
	private BookFormEventHandler handler;
	private static BookFormUiBinder uiBinder = GWT
			.create(BookFormUiBinder.class);

	interface BookFormUiBinder extends UiBinder<Widget, BookForm> {
	}

	public interface BookFormEventHandler{
		void onFormSuccess(BookRowObject adminObject);
		void onFormError(BookRowObject adminObject);
		void onAutoClose();
	}

	@UiField ParagraphElement pElement; 
	@UiField TextBox nameBox, fieldNameBox;
	@UiField Button submitBtn, addFieldBtn;
	@UiField RadioButton activeCheckBox, inActiveCheckBox;
	@UiField SpanElement formTitle;
	@UiField LIElement nameError, fieldNameError, groupError;
	@UiField CustomSuggestBox groupSuggest;
	//@UiField ComplexSortableList fieldSortableList;
	@UiField SpanElement closeBtn;
	@UiField(provided = true) ScrollableFlowPanel fieldsScrollPanel;
	@UiField(provided = true) ScrollableFlowPanel groupsScrollPanel;


	private BookRowObject bookObject;
	private boolean isAnUpdate = false;
	private PopupPanel popup;
	private ComplexSortableList fieldSortableList = new ComplexSortableList();
	private SortableList groupSortableList = new SortableList();

	public BookForm(BookFormEventHandler handler, PopupPanel popup) {
		this.popup = popup;
		this.handler = handler;
		fieldsScrollPanel = new ScrollableFlowPanel("book-field-style");
		groupsScrollPanel = new ScrollableFlowPanel("book-groups-style");

		initWidget(uiBinder.createAndBindUi(this));
		initBlackList();
		initComponents();
		initEvent();
	}

	public BookForm(BookFormEventHandler handler, BookRowObject bookObject, PopupPanel popup) {
		this.popup = popup;
		this.handler = handler;
		this.bookObject = bookObject;
		fieldsScrollPanel = new ScrollableFlowPanel("book-field-style");
		groupsScrollPanel = new ScrollableFlowPanel("book-groups-style");
		initWidget(uiBinder.createAndBindUi(this));
		initBlackList();
		initComponents();
		initEvent();
	}

	private void initBlackList(){
		forbiddenCharList = new ArrayList<String>();
		//blackListKeywordsList = new ArrayList<String>();

		for(String character : forbiddenChar){
			forbiddenCharList.add(character);
		}

//		for(String blockWord : blacklistKeywords){
//			blackListKeywordsList.add(blockWord.toLowerCase());
//		}
	}

	private void initComponents(){

		fieldsScrollPanel.add(fieldSortableList);
		groupsScrollPanel.add(groupSortableList);
		nameBox.getElement().setAttribute("placeholder", "Book Name");
		//emailBox.getElement().setAttribute("placeholder", "Enter email address");
		activeCheckBox.setText("Activate");
		inActiveCheckBox.setText("De-Activate");
		addFieldBtn.setText("Add Field");

		if(bookObject == null){
			formTitle.setInnerText("New Book");
			isAnUpdate = false;
			activeCheckBox.setValue(true);
		}else{
			isAnUpdate = true;
			formTitle.setInnerText("Edit Book");
			nameBox.setText(bookObject.getName().split("[:]")[1]);

			if(bookObject.getStatus().equals("A")){
				activeCheckBox.setValue(true);
				status = "A";
			}else{
				inActiveCheckBox.setValue(true);
				status = "D";
			}

			doBookGroupInit();
			doBookFieldInit();
		}

		groupSuggest.setLabel("Group(s)");
		groupSuggest.load(EntityEnum.GROUPS);

		initSuggestBoxEventHandler();
		initSortableListEvent();
		doFieldBoxConstraints();
	}
	
	private void doFieldBoxConstraints(){
		fieldNameBox.addKeyPressHandler(new KeyPressHandler() {
				
				@Override
				public void onKeyPress(KeyPressEvent event) {
					Widget sender = (Widget) event.getSource();
				    char charCode = event.getCharCode();
				    //int keyCode = event.getNativeEvent().getKeyCode();

				    if (!(charCode != '.' && charCode != '~' && charCode != '"' && charCode != ';' && charCode != ':' && charCode != '`' && charCode != '-' && charCode != '\\' && charCode != '|' && charCode != ',' && charCode != '%' &&  charCode != '@' &&  charCode != '#' &&  charCode != '$' &&  charCode != '^' &&  charCode != '&' &&  charCode != '*' && charCode != '!' &&  charCode != '{' &&  charCode != '}' &&  charCode != '(' &&  charCode != ')' &&  charCode != '[' &&  charCode != ']' &&  charCode != '+' &&  charCode != '=' &&  charCode != '\'' &&  charCode != '<' &&  charCode != '>' &&  charCode != '/' &&  charCode != '?')) {
				      ((TextBox) sender).cancelKey();
				    }			
				}
			});
			
		
	}

	private void initSuggestBoxEventHandler(){
		groupSuggest.setGroupSuggestBoxEventHandler(new CustomSuggestBoxEventHandler() {

			@Override
			public void onSuggestSelected(String suggestWord, String value, TextBoxBase base) {
				groupSortableList.add(new SortableObject(suggestWord, value));
				base.setText("");
			}
		});
	}

	private void doBookGroupInit(){
		if(bookObject.getGroups().trim().length() > 1){
			for(String groupId : bookObject.getGroups().trim().split(",")){
				groupSortableList.add(new SortableObject(Utils.getGroupName(groupId), groupId));
			}
		}else if(bookObject.getGroups().trim().length() == 1){
			groupSortableList.add(new SortableObject(Utils.getGroupName(bookObject.getGroups()), bookObject.getGroups()));
		}
	}

	private void doBookFieldInit(){
		if(bookObject.getColumnString().trim().length() > 1){
			String tableName = bookObject.getName().split(":")[0];
			for(String columnDetail : bookObject.getColumnString().trim().split("[,]")){
				String columnAlias = columnDetail.split(":")[1];
				String columnFieldName = columnDetail.split(":")[0];

				SortableObject sortable = new SortableObject(columnAlias, columnDetail);
				sortable.setFieldName(columnFieldName);

				fieldSortableList.add(tableName, sortable, false);
			}
		}
	}

	private void initSortableListEvent(){
		groupSortableList.setSortableListEventHandler(new SortableListEventHandler() {

			@Override
			public void onSortableRowEdit(SortableObject object) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSortableRowCanceled(SortableObject object) {
				// TODO Auto-generated method stub

			}
		});
	}
	
	private void doButtonDisable(boolean isDisable){
		if(isDisable){
			submitBtn.setText("Loading ...");
			submitBtn.setEnabled(false);
		}else{
			submitBtn.setEnabled(true);
			submitBtn.setText("Submit");
		}
	}

	private void initEvent(){
		submitBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				doButtonDisable(true);
				doValidation();
			}
		});

		inActiveCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(inActiveCheckBox.getValue()){
					activeCheckBox.setValue(false);
					status = "D";
				}else{
					activeCheckBox.setValue(true);
					status = "A";
				}
			}
		});

		activeCheckBox.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(activeCheckBox.getValue()){
					inActiveCheckBox.setValue(false);
					status = "A";
				}else{
					inActiveCheckBox.setValue(true);
					status = "D";
				}
			}
		});

		addFieldBtn.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if(fieldNameBox.getText().trim().isEmpty()){
					return;
				}

				if(isNotAllowed(fieldNameBox.getText().trim())){
					//Show error
					showError(fieldNameBox, fieldNameError, "invalid input", true);

					return;
				}

				showError(fieldNameBox, fieldNameError, "", false);
				fieldSortableList.add(new SortableObject(fieldNameBox.getText(), ""));
				fieldNameBox.setText("");
			}
		});

		fieldNameBox.addKeyPressHandler(new KeyPressHandler() {

			@Override
			public void onKeyPress(KeyPressEvent event) {
				if(event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER){

					if(isNotAllowed(fieldNameBox.getText().trim())){
						//Show error
						showError(fieldNameBox, fieldNameError, "invalid input", true);

						return;
					}

					showError(fieldNameBox, fieldNameError, "", false);
					fieldSortableList.add(new SortableObject(fieldNameBox.getText(), ""));
					fieldNameBox.setText("");
				}
			}
		});

		Element closeElement = closeBtn.cast();
		DOM.sinkEvents(closeElement, Event.ONCLICK);
		DOM.setEventListener(closeElement, new EventListener() {

			@Override
			public void onBrowserEvent(Event event) {
				if(popup != null){
					//popup.hide();
					showPopup();
				}
			}
		});

		fieldSortableList.setComplexSortableListEventHandler(new ComplexSortableListEventHandler() {

			@Override
			public void onSortableRowEdit(ColumnObject object) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onSortableRowCanceled(ColumnObject object) {
				//submitBtn.fireEvent(new ClickEvent(){});
				doPartialSave();
			}

			@Override
			public void onListItemSelected(String name, String value) {
				// TODO Auto-generated method stub

			}
		});
		
		popup.addCloseHandler(new CloseHandler<PopupPanel>() {
			
			@Override
			public void onClose(CloseEvent<PopupPanel> event) {
				if(handler != null){
					handler.onAutoClose();
				}
			}
		});
	}
	
	private void showPopup(){
		CustomConfirmationPanel innerPopup = new CustomConfirmationPanel();
		innerPopup.setCustomConfirmationPanelEventHandler(new CustomConfirmationPanelEventHandler() {
			
			@Override
			public void onContinueClicked() {
				popup.hide();
			}
			
			@Override
			public void onCancelClicked() {
				// TODO Auto-generated method stub
				
			}
		});
		innerPopup.show();
	}

	private boolean isNotAllowed(String word){
		//Check if character(s) is blacklist

		if(word != null){

			for(int i = 0; i < word.length(); i++){
				String charWord = word.charAt(i)+"";

				if(forbiddenCharList.contains(charWord)){
					return true;
				}
			}

//			if(blackListKeywordsList.contains(word.toLowerCase())){
//				return true;
//			}

			return false;
		}else{
			return true;
		}
	}

	protected void doValidation() {
		//Validate name
		switch(Utils.isNameValid(nameBox.getText().trim())){
		case VALUE_SHORT:
			showError(nameBox, nameError, "Name is too short", true);
			break;
		case BAD_REQUEST:
			break;
		case VALUE_EXISTS:
			showError(nameBox, nameError, "",  true);
			break;
		case VALUE_INVALID:
			showError(nameBox, nameError, "Invalid name format", true);
			break;
		case VALUE_ISEMPTY:
			showError(nameBox, nameError, "Name can not be empty", true);
			break;
		case VALUE_OK:
			showError(nameBox, nameError, "", false);
			doGroupValidation();
			break;
		default:
			break;
		}
	}

	private void doGroupValidation(){
		int size = groupSortableList.getSelectedSortableHashMap().size();
		if(size <= 0){
			//show error
			showError(groupSuggest, groupError, "select at least one group", true);
			return;
		}else{
			//Proceed to save
			showError(groupSuggest, groupError, "", false);
			doSave();
		}
	}

	private BookRowObject getNewPreparedBookObject(BookRowObject object){
		object.setName(Utils.getCompoundedName(object.getName())+":"+Utils.getDecodedCompoundedName(object.getName()));

		return object;
	}

	private BookRowObject getUpdatedPreparedBookObject(BookRowObject object, String newString){
		object.setName(object.getName().split(":")[0]+":"+newString);

		return object;
	}
	
	private void doUpdateSave(){
		submitBtn.setText("Loading ...");
		GlobalResource.getInstance().getAdminUpdateRPC().isBooksUpdated(getUpdatedPreparedBookObject(bookObject, nameBox.getText().trim()), new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if(result){
					doButtonDisable(false);
					if(handler != null){
						handler.onFormSuccess(bookObject);
					}
				}else{
					doButtonDisable(false);

					if(handler != null){
						//handler.onFormError(bookObject);
						//Display error
						showEntryError();
					}
				}
				//handler.onFormSuccess(bookObject);
			}

			@Override
			public void onFailure(Throwable caught) {
				//handler.onFormError(bookObject);
				showEntryError();
				doButtonDisable(false);
			}
		});
	}
	
	private void showEntryError(){
		pElement.setAttribute("style", "color:red;");
		pElement.setInnerText("Book name or field name already exist");
	}

	private void doSave(){
		if(bookObject == null){
			bookObject = new BookRowObject();
			bookObject.setName(Utils.getCompoundedName(nameBox.getText().trim()));
			bookObject.setCreatedBy(GlobalResource.getInstance().getUser().getId());
		}

		bookObject.setModifiedBy(GlobalResource.getInstance().getUser().getId());
		bookObject.setStatus(status);
		bookObject.setGroups(groupSortableList.getSelectedKeyString());

		GWT.log("Book data is "+"createdBy :"+bookObject.getCreatedBy()+", status is "+bookObject.getStatus()+", name is "+bookObject.getName()+", groups is "+bookObject.getGroups()+", columns is"+bookObject.getColumnString());

		if(isAnUpdate){
			bookObject.setColumnString(fieldSortableList.getUpdateKeyString());
			bookObject.setAddColumnString(fieldSortableList.getSelectedKeyString());

			GWT.log("updatedColumnString is "+bookObject.getColumnString());
			GWT.log("newColumnString is "+bookObject.getAddColumnString());

			Utils.isTableExist(nameBox.getText().trim(), bookObject.getId(), new RPCCompleteHandler<ErrorIndicator>() {

				@Override
				public void onProccessComplete(ErrorIndicator t) {
					switch(t){
					case BAD_REQUEST:
						break;
					case VALUE_EXISTS:
						showError(nameBox, nameError, "Sorry, Book name already exists", true);
						bookObject = null;
						break;
					case VALUE_INVALID:
						break;
					case VALUE_ISEMPTY:
						break;
					case VALUE_OK:
						doUpdateSave();
						break;
					case VALUE_SHORT:
						break;
					case VALUE_TOO_LONG:
						break;
					default:
						break;
					}
				}
			});
			
		}else{

			bookObject.setColumnString(fieldSortableList.getSelectedKeyString());
			Utils.isTableExist(nameBox.getText().trim(), bookObject.getId(), new RPCCompleteHandler<ErrorIndicator>() {

				@Override
				public void onProccessComplete(ErrorIndicator t) {
					switch(t){
					case BAD_REQUEST:
						break;
					case VALUE_EXISTS:
						showError(nameBox, nameError, "Sorry, Book name already exists", true);
						bookObject = null;
						break;
					case VALUE_INVALID:
						break;
					case VALUE_ISEMPTY:
						break;
					case VALUE_OK:
						doNewBookRPC();
						break;
					case VALUE_SHORT:
						break;
					case VALUE_TOO_LONG:
						break;
					default:
						break;
					}
				}
			});

		}
	}

	private void doPartialSave(){
		if(bookObject == null){
			bookObject = new BookRowObject();
			bookObject.setName(Utils.getCompoundedName(nameBox.getText().trim()));
			bookObject.setCreatedBy(GlobalResource.getInstance().getUser().getId());
		}

		bookObject.setModifiedBy(GlobalResource.getInstance().getUser().getId());
		bookObject.setStatus(status);


		bookObject.setColumnString(fieldSortableList.getUpdateKeyString());
		bookObject.setAddColumnString(fieldSortableList.getSelectedKeyString());


		GWT.log("Book data is "+"modified :"+bookObject.getModifiedBy()+", status is "+bookObject.getStatus()+", name is "+bookObject.getName()+", groups is "+bookObject.getGroups()+", columns is"+bookObject.getColumnString());
		
		GWT.log("updatedColumnString is "+bookObject.getColumnString());
		
		GlobalResource.getInstance().getAdminUpdateRPC().isBookColumnStringPartiallyUpdated(bookObject, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFailure(Throwable caught) {
				doButtonDisable(false);
				
			}
		});

	}

	private void doNewBookRPC(){
		submitBtn.setText("Loading ...");
		GlobalResource.getInstance().getAdminNewRPC().isBooksCreated(getNewPreparedBookObject(bookObject), new AsyncCallback<Boolean>() {

			@Override
			public void onSuccess(Boolean result) {
				if(result){
					doButtonDisable(false);
					if(handler != null){
						handler.onFormSuccess(bookObject);
					}
				}else{
					doButtonDisable(false);
					
					if(handler != null){
						//handler.onFormError(bookObject);
						showEntryError();
					}
				}
				//handler.onFormSuccess(bookObject);

			}

			@Override
			public void onFailure(Throwable caught) {
				doButtonDisable(false);

			}
		});
	}

	private void showError(Widget widget, LIElement label, String errorLabel, boolean isShowError){

		final String ERROR_CLASS = "parsley-error";

		label.setInnerText(errorLabel);
		if(isShowError){
			widget.getElement().addClassName(ERROR_CLASS);
			label.setAttribute("style", "display: block; color: #ff5f5f");
			doButtonDisable(false);
		}else{
			widget.getElement().removeClassName(ERROR_CLASS);
			label.setAttribute("style", "display: none;");
		}
	}

}
