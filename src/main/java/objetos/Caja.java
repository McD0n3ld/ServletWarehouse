package objetos;

public class Caja {

	private String code;
	private String contents;
	private int value;
	private int code_warehouse;
	//campo opcional no necesario (solo informativo)
	private String warehouse_location;
	private boolean estado;	//false = en ninguna warehouse ; true = en alguna warehouse

	public boolean getEstado() {
		return estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getWarehouse_location() {
		return warehouse_location;
	}

	public void setWarehouse_location(String warehouse_location) {
		this.warehouse_location = warehouse_location;
	}

	public Caja() {
		// TODO Auto-generated constructor stub
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getCode_warehouse() {
		return code_warehouse;
	}

	public void setCode_warehouse(int code_warehouse) {
		this.code_warehouse = code_warehouse;
	}

}
