package riscv
import chisel3._

class if_id_reg extends Module{

	val io = IO(new Bundle {
		val pc_input = Input(UInt(32.W))
		val pc4_input = Input(UInt(32.W))
		val instruction_input = Input(UInt(32.W))

		val pc_output = Output(UInt(32.W))
		val pc4_output = Output(UInt(32.W))
		val instruction_output = Output(UInt(32.W))

})

	val pc_register = RegInit(0.U(32.W))
	val pc4_register = RegInit(0.U(32.W))
	val ins_reg = RegInit(0.U(32.W))

	pc_register := io.pc_input
	pc4_register := io.pc4_input
	ins_reg := io.instruction_input

	io.pc_output := pc_register
	io.pc4_output := pc4_register
	io.instruction_output := ins_reg


}
