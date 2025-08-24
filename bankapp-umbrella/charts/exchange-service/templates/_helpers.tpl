cl{{- define "exchange-service.name" -}}
{{ .Chart.Name }}
{{- end }}

{{- define "exchange-service.fullname" -}}
{{ .Release.Name }}-{{ .Chart.Name }}
{{- end }}

{{- define "exchange-service.selectorLabels" -}}
app: {{ include "cash-service.name" . }}
{{- end }}

{{- define "exchange-service.labels" -}}
app: {{ include "exchange-service.name" . }}
chart: {{ .Chart.Name }}-{{ .Chart.Version }}
release: {{ .Release.Name }}
heritage: {{ .Release.Service }}
{{- end }}