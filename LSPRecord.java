public class LSPRecord
{
    LSP2 lsp;
    int sent;

    LSPRecord(final LSP2 lsp,final int sent)
    {
        this.lsp = lsp;
        this.sent = sent;
    }

    @Override
    public String toString() {
        return lsp.toString() + ":" + Integer.toString(sent);
    }
}
